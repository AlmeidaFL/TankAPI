package org.converters

import org.model.Space
import org.web.Resource

class SpaceConverter() : BaseConverter<Space, Resource>() {
  override fun convertToLeft(right: Resource): Space {
    return Space(null, right.getString("name"), right.getString("owner"))
  }

  override fun convertToRight(left: Space): Resource {
    throw Exception("Should not convert to resource")
  }
}
