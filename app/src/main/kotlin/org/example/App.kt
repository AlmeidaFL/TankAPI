package org.example

import com.google.common.util.concurrent.RateLimiter
import org.controller.ApiUserController
import org.controller.AuditController
import org.controller.SpaceController
import org.core.exceptions.UserNotAuthenticatedException
import org.core.services.*
import org.dalesbred.result.EmptyResultException
import org.json.JSONException
import org.json.JSONObject
import org.persistence.DatabaseCreator
import org.persistence.repositories.*
import org.persistence.savers.*
import org.web.*
import spark.Request
import spark.Response
import spark.Spark.*
import spark.kotlin.before
import spark.kotlin.secure

fun main() {
    var main = Main().run()
}

class Main() {

    fun run() {
        addTlsSuport()
        setRateLimiter()
        val database = DatabaseCreator.createDatabase("/schemas.sql")
        val spaceController = SpaceController(SpaceService(SpaceRepository(SpaceSaver(database))))
        val userService = UserService(ApiUserRepository(ApiUserSaver(database)))
        val userController = ApiUserController(userService)
        val auditController = AuditController(AuditLogService(AuditLogDAO(database)))

        addBefore(auditController, userService)
        setEndpoints(spaceController, userController, auditController)
        setExceptionErrors()
        setGeneralErrors()
        addProtectionHeaders()
        addAfter(auditController)
    }

    private fun addAfter(auditController: AuditController) {
        afterAfter(auditController::logRequestExit)
    }

    private fun addBefore(auditController: AuditController, userService: UserService) {

        // Filters
        before {
            if (this.requestMethod() != HttpMethod.POST.name &&
                this.request.contentType() != MediaTypes.JSON.name
            ) {
                halt(415, JSONObject().put("error", "Only application/json supported").toString())
            }
        }
        spark.kotlin.before(userService::authenticate)
        spark.kotlin.before(auditController::logRequestEntry)
    }

    private fun addTlsSuport() {
        secure("localhost.p12", "changeit", null, null)
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

    private fun addProtectionHeaders() {
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
        // afterAfter {_, response -> response.header("Strict-Transport-Security", "max-age:3153600")}
    }

    private fun setGeneralErrors() {
        internalServerError(JSONObject().put("error", "internal server error").toString())
        notFound(JSONObject().put("error", "not found").toString())
    }

    private fun setEndpoints(
        spaceController: SpaceController,
        userController: ApiUserController,
        auditController: AuditController
    ) {
        post("/spaces", spaceController::createSpace)
        post("/users", userController::createUser)
        get("/logs", auditController::getLogsByHour)
        after("/") { _, response -> response.type("application/json") }
    }

    private fun setExceptionErrors() {
        exception(IllegalArgumentException::class.java, ::badRequest)
        exception(JSONException::class.java, ::badRequest)
        exception(EmptyResultException::class.java) { _, _, response -> response.status(404) }
        exception(UserNotAuthenticatedException::class.java, ::badRequest)
    }

    private fun badRequest(exception: Exception, request: Request, response: Response) {
        response.status(400)
        response.body(JSONObject().put("error", exception.message).toString())
    }
}
