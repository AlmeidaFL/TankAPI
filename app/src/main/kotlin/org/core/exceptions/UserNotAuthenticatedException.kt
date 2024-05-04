package org.core.exceptions

class UserNotAuthenticatedException(override val message: String = "User is not authenticated") :
        Exception(message)
