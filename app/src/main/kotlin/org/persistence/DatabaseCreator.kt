package org.persistence

import java.nio.file.Files
import java.nio.file.Paths

class DatabaseCreator {

    companion object{
        fun createDatabase(schemaPath: String): H2 {
            val path = Paths.get(this::class.java.getResource(schemaPath).toURI())
            val schema = Files.readString(path)
            return createH2(schema)
        }
    }
}

fun createH2(schemaPath: String): H2 {
    return createH2 { schema = schemaPath }
}
