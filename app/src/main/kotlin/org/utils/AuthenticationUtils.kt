package org.utils

import org.core.exceptions.UserNotAuthenticatedException

class AuthenticationUtils {
  companion object {
    fun validateAuthentication(user: String, subject: String?) {
      if (subject == null || subject != user) {
        throw UserNotAuthenticatedException()
      }
    }
  }
}
