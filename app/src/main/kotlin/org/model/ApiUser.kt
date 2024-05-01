package org.model

class ApiUser(var id: Long, name: String, var hashPassword: String) : User(name)
