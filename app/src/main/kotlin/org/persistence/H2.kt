package org.persistence

import java.util.*
import org.dalesbred.Database
import org.dalesbred.integration.kotlin.findOptional
import org.h2.jdbcx.JdbcConnectionPool

class H2 {
  private var database: Database? = null
    get() = field
    set(value) {
      if (database == null) field = value
    }
  var schema = ""

  fun insert(idQuery: String, placeHolderQuery: String, vararg values: String): Long {
    var id = -1L
    try {
      database!!.withTransaction {
        id = database!!.findUniqueLong(idQuery)
        database!!.updateUnique(placeHolderQuery, id, *values)
      }
    } catch (e: Exception) {
      id = -1
      throw e
    }
    return id
  }

  fun <T> query(placeHolderQuery: String, vararg values: Any, cl: Class<T>): Optional<T>{
    return database!!.findOptional(cl, placeHolderQuery, values)
  }

  internal fun initialize() {
    var datasource = JdbcConnectionPool.create("jdbc:h2:mem:tank", "tank", "password")
    database = Database.forDataSource(datasource)
    database!!.update(schema)
    datasource = JdbcConnectionPool.create("jdbc:h2:mem:tank", "tank_api_user", "password")
    database = Database.forDataSource(datasource)
  }
}

fun createH2(creator: H2.() -> Unit): H2 {
  val database = H2()
  database.creator()
  database.initialize()
  return database
}
