package org.model

class ApiUser(var id: Long = -1, name: String, var password: String) : User(name)
