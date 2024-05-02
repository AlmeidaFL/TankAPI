package org.persistence.savers

import java.util.Optional
import org.model.ApiUser
import org.persistence.H2

class ApiUserSaver(private val database: H2) : Saver<ApiUser> {

        override fun save(value: ApiUser) {
                val id =
                                database.insert(
                                                idQuery = "SELECT NEXT VALUE FOR user_id",
                                                placeHolderQuery = "INSERT INTO User VALUES(?,?,?)",
                                                value.name,
                                                value.password
                                )
                value.id = id
        }

        fun getPasswordHash(id: String): Optional<String> {
                return database.query(
                                placeHolderQuery = "SELECT * FROM users WHERE user_id = ?",
                                values = arrayOf(id),
                                cl = String::class.java
                )
        }
}
