package org.controller

import org.converters.SpaceConverter
import org.core.services.SpaceService
import org.core.validation.UserValidator
import org.json.*
import org.model.User
import org.web.Resource
import spark.Request
import spark.Response

class SpaceController(private var spaceService: SpaceService) {
  fun createSpace(request: Request, response: Response): JSONObject {
    var resource = Resource(request.body())
    var space = SpaceConverter().convertToLeft(resource)

    val infos = UserValidator().validate(User(space.owner))
    spaceService.saveSpace(space)
    response.status(201)
    response.header("Location", "/spaces/${space.id}")

    return JSONObject().put("name", space.name).put("uri", "/spaces/${space.id}")
  }
}
