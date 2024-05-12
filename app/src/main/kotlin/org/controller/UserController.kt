package org.controller

import com.lambdaworks.crypto.SCryptUtil
import org.converters.ApiUserConverter
import org.core.services.UserService
import org.core.validation.compound.ApiUserCompound
import org.json.JSONObject
import org.web.HttpHeader
import org.web.Resource
import spark.Request
import spark.Response
import spark.kotlin.halt

class ApiUserController(private val userService: UserService) {

  fun createUser(request: Request, response: Response): JSONObject {
    var apiUser = ApiUserConverter().convertToLeft(Resource(request.body()))
    ApiUserCompound.validate(apiUser)
    val hash = SCryptUtil.scrypt(apiUser.password, 32768, 8, 1)
    apiUser.password = hash
    userService.saveUser(apiUser)

    response.status(201)
    response.header("Location", "/users/${apiUser.name}")

    return JSONObject().put("username", apiUser.name)
  }

  fun requiresAuthentication(request: Request, response: Response) {
    if (request.attribute<String>("subject") == null) {
      response.header(HttpHeader.WWWAUTH.type, "Basic realm=\"/\", charset=\"UTF-8\"")
      halt(401)
    }
  }
}
