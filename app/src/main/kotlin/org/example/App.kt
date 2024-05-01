package org.example

import com.google.common.util.concurrent.RateLimiter
import org.controller.SpaceController
import org.core.services.SpaceService
import org.dalesbred.result.EmptyResultException
import org.json.JSONException
import org.json.JSONObject
import org.persistence.DatabaseCreator
import org.persistence.repositories.SpaceRepository
import org.persistence.savers.SpaceSaver
import org.web.*
import spark.Request
import spark.Response
import spark.Spark.*
import spark.kotlin.before

fun main() {
  var main = Main().run()
}

class Main() {

  fun run() {
    setRateLimiter()
    val database = DatabaseCreator.createDatabase("/schemas.sql")
    val spaceController = SpaceController(SpaceService(SpaceRepository(SpaceSaver(database))))

    // Filters
    before {
      if (this.requestMethod() != HttpMethod.POST.name &&
              this.request.contentType() != MediaTypes.JSON.name
      ) {
        halt(415, JSONObject().put("error", "Only application/json supported").toString())
      }
    }
    setEndpoints(spaceController)
    setExceptionErrors()
    setGeneralErrors()
    removeUnsafeHeaders()
  }

  private fun setRateLimiter() {
    val rateLimiter = RateLimiter.create(10.0)
    before {
      if (!rateLimiter.tryAcquire()) {
        this.response.header("Retry-After", "2")
        halt(429)
      }
    }
  }

  private fun removeUnsafeHeaders() {
    afterAfter { _, response -> response.header("Server", "") }
    afterAfter { _, response -> response.type("application/json;charset=utf-8") }
    afterAfter { _, response -> response.header("X-XSS-Protection", "0") } // OWASP recommendation
    afterAfter { _, response -> response.header("X-Content-Type-Options", "no-sniff") } // Avoid XSS
    afterAfter { _, response -> response.header("X-Frame-Options", "DENY") } // Avoid XSS
    afterAfter { _, response -> response.header("Cache-Control", "no-store") }
    afterAfter { _, response ->
      response.header(
          "Content-Security-Policy",
          "default-src 'none'; frame-ancestors 'none'; sandbox"
      )
    } // Avoid loading script, response into iframe
    // and disable script execution
  }

  private fun setGeneralErrors() {
    internalServerError(JSONObject().put("error", "internal server error").toString())
    notFound(JSONObject().put("error", "not found").toString())
  }

  private fun setEndpoints(spaceController: SpaceController) {
    post("/spaces", spaceController::createSpace)
    after("/") { _, response -> response.type("application/json") }
  }

  private fun setExceptionErrors() {
    exception(IllegalArgumentException::class.java, ::badRequest)
    exception(JSONException::class.java, ::badRequest)
    exception(EmptyResultException::class.java) { _, _, response -> response.status(404) }
  }

  private fun badRequest(exception: Exception, request: Request, response: Response) {
    response.status(400)
    response.body(JSONObject().put("error", exception.message).toString())
  }
}
