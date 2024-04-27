package org.persistence

import java.nio.file.Files
import java.nio.file.Paths

enum class Databases {
  H2
}

class DatabaseCreator {

  fun createDatabase(database: Databases, schemaPath: String): IDatabase {
    var path = Paths.get(this::class.java.getResource(schemaPath).toURI())
    var schema = Files.readString(path)

    when (database) {
      Databases.H2 -> return createH2(schema)
    }
  }
}

fun createH2(schemaPath: String): IDatabase {
  return createH2 { schema = schemaPath }
}

