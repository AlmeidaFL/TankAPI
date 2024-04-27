package org.persistence.repositories

import org.model.Space
import org.persistence.savers.SpaceSaver

class SpaceRepository(private var spaceSaver: SpaceSaver) {
  fun insertSpace(space: Space) {
    spaceSaver.save(space)
  }
}
