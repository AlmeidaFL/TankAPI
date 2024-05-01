package org.converters

import org.model.ApiUser
import org.web.Resource

class ApiUserConverter() : BaseConverter<ApiUser, Resource>() {
  override fun convertToLeft(right: Resource): ApiUser {
    return ApiUser(name = right.getString("username"), password = right.getString("password"))
  }

  override fun convertToRight(left: ApiUser): Resource {
    throw Exception("Should not convert to resource")
  }
}
