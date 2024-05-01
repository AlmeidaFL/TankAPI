package org.controller

import com.lambdaworks.crypto.SCryptUtil
import org.converters.ApiUserConverter
import org.core.services.UserService
import org.core.validation.compound.ApiUserCompound
import org.json.JSONObject
import org.web.Resource
import spark.Request
import spark.Response

class UserController(private val userService: UserService) {
  fun createUser(request: Request, response: Response): JSONObject {
    var apiUser = ApiUserConverter().convertToLeft(Resource(request.body()))
    ApiUserCompound.validate(apiUser)
    val hash = SCryptUtil.scrypt(apiUser.password, 32768, 8, 1)
    apiUser.password = hash
    userService.saveUser(apiUser)

    response.status(201)
    response.header("Location", "/users/${apiUser.userName}")

    return JSONObject().put("username", apiUser.userName)
  }
}
