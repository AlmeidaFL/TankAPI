package org.example

import org.controller.SpaceController
import org.core.services.SpaceService
import org.dalesbred.result.EmptyResultException
import org.json.JSONException
import org.json.JSONObject
import org.persistence.DatabaseCreator
import org.persistence.repositories.SpaceRepository
import org.persistence.savers.SpaceSaver
import spark.Request
import spark.Response
import spark.Spark.*

fun main() {
  var main = Main().run()
}

class Main() {

  fun run() {
    val database = DatabaseCreator.createDatabase("/schemas.sql")
    val spaceController = SpaceController(SpaceService(SpaceRepository(SpaceSaver(database))))
    setEndpoints(spaceController)
    setExceptionErrors()
    setGeneralErrors()
  }

  private fun setGeneralErrors() {
    internalServerError(JSONObject().put("error", "internal server error").toString())
    notFound(JSONObject().put("error", "not found").toString())
  }

  private fun setEndpoints(spaceController: SpaceController) {
    post("/spaces", spaceController::createSpace)
    after("/") { request, response -> response.type("application/json") }
  }

  private fun setExceptionErrors() {
    exception(IllegalArgumentException::class.java, ::badRequest)
    exception(JSONException::class.java, ::badRequest)
    exception(EmptyResultException::class.java) { exception, request, response ->
      response.status(404)
    }
  }

  private fun badRequest(exception: Exception, request: Request, response: Response) {
    response.status(400)
    response.body("{\"error\": \"${exception.message}\"}")
  }
}
