package org.core.validation.compound

import org.core.validation.ApiUserValidator
import org.core.validation.UserValidator
import org.model.ApiUser
import org.model.User

sealed class ApiUserCompound {
  companion object {
    fun validate(apiUser: ApiUser) {
      Compound<User>()
          .apply {
            addValidator(UserValidator())
            addValidator(ApiUserValidator())
          }
          .validate(apiUser)
    }
  }
}
