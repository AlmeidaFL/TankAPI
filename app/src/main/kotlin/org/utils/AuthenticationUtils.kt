package org.utils

import org.core.exceptions.UserNotAuthorizedException

class AuthenticationUtils {
  companion object {
    fun validateAuthentication(user: String, subject: String?) {
      if (subject != user) {
        throw UserNotAuthorizedException()
      }
    }
  }
}
