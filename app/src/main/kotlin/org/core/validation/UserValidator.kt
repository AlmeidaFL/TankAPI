package org.core.validation

import kotlin.collections.mutableListOf
import org.model.User

class UserValidator : Validator<User> {
  override fun validate(input: User): List<Information> {
    val information = mutableListOf<Information>()

    if (!input.name.matches(Regex("[a-zA-Z][a-zA-Z0-9]{1,29}"))) {
      information.add(Information("Invalid user name", InfoType.ERROR))
    }

    return information
  }
}
