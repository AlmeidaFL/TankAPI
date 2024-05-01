package org.core.validation

import org.model.ApiUser

class ApiUserValidator : Validator<ApiUser> {
  override fun validate(input: ApiUser) {
    if (input.password.length < 12) {
      throw IllegalArgumentException("Password must be more than 11 characters")
    }
  }
}
