package org.persistence.repositories

import org.model.ApiUser
import org.persistence.savers.ApiUserSaver

class ApiUserRepository(private val userSaver: ApiUserSaver) {
  fun insertUser(user: ApiUser) {
    userSaver.save(user)
  }
}
