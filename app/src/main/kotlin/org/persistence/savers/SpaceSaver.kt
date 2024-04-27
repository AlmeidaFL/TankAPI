package org.persistence.savers

import org.model.Space
import org.persistence.IDatabase

class SpaceSaver(private var database: IDatabase) : Saver<Space> {
  override fun save(value: Space) {
    var id = database.insert(getIdString = "SELECT NEXT VALUE FOR space_id_seq"){
        "INSERT INTO spaces(space_id, name, owner) VALUES (${it}, '${value.name}', '${value.owner}')"
    }
      value.id = id
  }
}
