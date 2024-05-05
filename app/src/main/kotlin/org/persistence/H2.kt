package org.persistence

import java.util.*
import org.dalesbred.Database
import org.h2.jdbcx.JdbcConnectionPool
import java.sql.ResultSet

class H2 {
  private var database: Database? = null
    get() = field
    set(value) {
      if (database == null) field = value
    }
  var schema = ""

  fun insert(placeHolderQuery: String, vararg values: String) {
    database!!.withTransaction { database!!.updateUnique(placeHolderQuery, *values) }
  }

  fun insertWithTransaction(idQuery: String, placeHolderQuery: String, vararg values: Any): Long {
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

  fun insertWithVoid(idQuery: String, placeHolderQuery: String, vararg values: Any?): Long {
    var id = -1L
    try {
      database!!.withVoidTransaction {
        id = database!!.findUniqueLong(idQuery)
        database!!.updateUnique(placeHolderQuery, id, *values)
      }
    } catch (e: Exception) {
      id = -1
      throw e
    }
    return id
  }

  fun <T> query(placeHolderQuery: String, cl: Class<T>, vararg values: Any): Optional<T> {
    return database!!.findOptional(cl, placeHolderQuery, values)
  }
  
  fun <T> queryAll(placeHolderQuery: String, rowMapper: (ResultSet) -> T, vararg values: Any): List<T> {
      return database!!.findAll(rowMapper, placeHolderQuery, values)
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
