package org.core.validation

import org.model.Space

class SpaceValidator : Validator<Space> {
  override fun validate(input: Space) {
    if (input.name.length > 255) {
      throw IllegalArgumentException("Space name too long")
    }
  }
}
