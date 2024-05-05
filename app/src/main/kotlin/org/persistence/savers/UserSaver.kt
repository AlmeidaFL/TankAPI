package org.persistence.savers

import java.util.Optional
import org.model.ApiUser
import org.persistence.H2

class ApiUserSaver(private val database: H2) : Saver<ApiUser> {

                override fun save(value: ApiUser) {
                                database.insert(
                                                                placeHolderQuery =
                                                                                                "INSERT INTO users VALUES(?,?)",
                                                                value.name,
                                                                value.password,
                                )
                }

                fun getPasswordHash(id: String): Optional<String> {
                                return database.query(
                                                                placeHolderQuery =
                                                                                                "SELECT password_hash FROM users WHERE id = ?",
                                                                values = arrayOf(id),
                                                                cl = String::class.java
                                )
                }
}
