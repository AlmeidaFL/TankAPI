package org.persistence

import org.dalesbred.Database
import org.h2.jdbcx.JdbcConnectionPool

class H2 : IDatabase() {
  var database: Database? = null
    get() = field
    set(value) {
      if (database == null) field = value
    }
  public var schema = ""

  override fun insert(getIdString: String?, query: (id: Long) -> String): Long {
    if (getIdString != null){
      val id = database!!.findUniqueLong(getIdString)
      database!!.updateUnique(query(id))
      return id
    }
    return -1
  }

  override fun insert(query: String, vararg values: String) {}

  override fun getEntityId(query: String): Long {
    return database!!.findUniqueLong(query)
  }

  internal fun initialize() {
    var datasource = JdbcConnectionPool.create("jdbc:h2:mem:tank", "tank", "password")
    database = Database.forDataSource(datasource)
    database!!.update(schema)
  }
}

fun createH2(creator: H2.() -> Unit): H2 {
  var database = H2()
  database.creator()
  database.initialize()
  return database
}
