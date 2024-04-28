package org.core.validation

import org.model.User

class UserValidator : Validator<User> {
  override fun validate(input: User) {
    if (!input.name.matches(Regex("[a-zA-Z][a-zA-Z0-9]{1,29}"))) {
      throw IllegalArgumentException(
          "Invalid user name"
      ) // It's best practive to not return request input avoiding XSS
    }
  }
}
