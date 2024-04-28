package org.core.validation

import kotlin.collections.mutableListOf
import org.model.User

class UserValidator : Validator<User> {
  override fun validate(input: User){
    if (!input.name.matches(Regex("[a-zA-Z][a-zA-Z0-9]{1,29}"))) {
      throw IllegalArgumentException("Invalid user name")
    }
  }
}
