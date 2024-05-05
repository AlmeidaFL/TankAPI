package org.persistence.savers

import org.model.Space
import org.persistence.H2

class SpaceSaver(private var database: H2) : Saver<Space> {
  override fun save(value: Space) {
    val id = database.insertWithTransaction(
      idQuery = "SELECT NEXT VALUE FOR space_id_seq",
      placeHolderQuery = "INSERT INTO spaces(id, name, owner) VALUES (?, ?, ?)",
      value.name,
      value.owner
    )
      value.id = id
  }
}
