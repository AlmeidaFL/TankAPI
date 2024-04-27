package org.core.services

import org.model.Space
import org.persistence.repositories.SpaceRepository

class SpaceService(private var repository: SpaceRepository) {
  fun saveSpace(space: Space) {
    repository.insertSpace(space)
  }
}
