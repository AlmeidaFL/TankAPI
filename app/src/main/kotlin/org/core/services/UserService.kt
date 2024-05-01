package org.core.services

import org.model.ApiUser
import org.persistence.repositories.ApiUserRepository

class UserService(val repository: ApiUserRepository) {
  fun saveUser(user: ApiUser) {
    repository.insertUser(user)
  }
}
