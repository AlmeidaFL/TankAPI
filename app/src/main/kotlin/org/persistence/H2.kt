package org.persistence

import org.dalesbred.Database
import org.h2.jdbcx.JdbcConnectionPool
import java.util.*

class H2 {
  var database: Database? = null
    get() = field
    set(value) {
      if (database == null) field = value
    }
   var schema = ""

  fun insert(idQuery: String, placeHolderQuery: String, vararg values: String): Long{
    var id = -1L
    try {
      database!!.withTransaction {
        id = database!!.findUniqueLong(idQuery)
        database!!.updateUnique(placeHolderQuery, id, *values)
      }
    }catch (e: Exception){
      id = -1
      throw e
    }
    return id
  }

  internal fun initialize() {
    val datasource = JdbcConnectionPool.create("jdbc:h2:mem:tank", "tank", "password")
    database = Database.forDataSource(datasource)
    database!!.update(schema)
  }
}

fun createH2(creator: H2.() -> Unit): H2 {
  val database = H2()
  database.creator()
  database.initialize()
  return database
}
