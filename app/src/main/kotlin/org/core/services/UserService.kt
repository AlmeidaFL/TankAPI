package org.core.services

import com.lambdaworks.codec.Base64
import com.lambdaworks.crypto.SCryptUtil
import org.core.exceptions.UserNotAuthenticatedException
import java.nio.charset.StandardCharsets
import kotlin.check
import org.model.ApiUser
import org.persistence.repositories.ApiUserRepository
import spark.Request
import spark.Response

const val BASIC = "Basic"

class UserService(val repository: ApiUserRepository) {
  fun saveUser(user: ApiUser) {
    repository.insertUser(user)
  }

  fun authenticate(request: Request, response: Response) {
    val basicAuthenticationHeader = request.headers("Authorization")

    if (basicAuthenticationHeader == null || !basicAuthenticationHeader.startsWith(BASIC)) {
      return
    }

    val credentialsOffset = 1
    val base64Credentials = basicAuthenticationHeader.substring(BASIC.length + credentialsOffset)
    val splitCredentials =
        String(Base64.decode(base64Credentials.toCharArray()), StandardCharsets.UTF_8).split(":")

    val userId = splitCredentials[0]
    val password = splitCredentials[1]

    val hashedPassword = repository.getPasswordHash(userId)
    if (hashedPassword.isPresent() && SCryptUtil.check(password, hashedPassword.get())) {
      request.attribute("subject", userId)
      return
    }
  }
}
