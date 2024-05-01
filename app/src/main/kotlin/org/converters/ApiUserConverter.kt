package org.converters

import org.model.ApiUser
import org.web.Resource

class ApiUserConverter() : BaseConverter<ApiUser, Resource>() {
  override fun convertToLeft(resource: Resource): ApiUser {
    return ApiUser(w, name, hashPassword)
  }
}
