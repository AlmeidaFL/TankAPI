package org.persistence

abstract class IDatabase {
  abstract fun insert(
      query: String,
      vararg values: String,
  )

  abstract fun insert(getIdString: String?, query: (id: Long) -> String): Long

  abstract fun getEntityId(query: String): Long
}
