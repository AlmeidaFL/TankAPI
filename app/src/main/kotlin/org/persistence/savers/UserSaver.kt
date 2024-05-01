package org.persistence.savers

import org.model.ApiUser
import org.persistence.H2

class ApiUserSaver(private val database: H2) : Saver<ApiUser> {

    override fun save(value: ApiUser) {
        val id =
                database.insert(
                        idQuery = "SELECT NEXT VALUE FOR user_id",
                        placeHolderQuery = "INSERT INTO User VALUES(?,?,?)",
                        value.name,
                        value.hashPassword
                )
        value.id = id
    }
}
