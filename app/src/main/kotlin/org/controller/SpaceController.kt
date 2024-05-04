package org.controller

import org.converters.SpaceConverter
import org.core.services.SpaceService
import org.core.validation.SpaceValidator
import org.core.validation.UserValidator
import org.json.*
import org.model.SpaceCreator
import org.utils.AuthenticationUtils
import org.web.Resource
import spark.Request
import spark.Response

class SpaceController(private var spaceService: SpaceService) {
  fun createSpace(request: Request, response: Response): JSONObject {
    var resource: Resource?
    try {
      resource = Resource(request.body())
    } catch (e: Exception) {
      throw IllegalArgumentException("Resource is in bad format")
    }
    var space = SpaceConverter().convertToLeft(resource)
    AuthenticationUtils.validateAuthentication(space.owner, request.attribute("subject"))
    UserValidator().validate(SpaceCreator(space.owner))
    SpaceValidator().validate(space)
    spaceService.saveSpace(space)

    response.status(201)
    response.header("Location", "/spaces/${space.id}")

    return JSONObject().put("name", space.name).put("uri", "/spaces/${space.id}")
  }
}
