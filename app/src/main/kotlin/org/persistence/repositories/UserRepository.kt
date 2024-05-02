package org.persistence.repositories

import java.util.Optional
import org.model.ApiUser
import org.persistence.savers.ApiUserSaver

class ApiUserRepository(private val userSaver: ApiUserSaver) {
  fun insertUser(user: ApiUser) {
    userSaver.save(user)
  }

  fun getPasswordHash(id: String): Optional<String> {
    return userSaver.getPasswordHash(id)
  }
}
