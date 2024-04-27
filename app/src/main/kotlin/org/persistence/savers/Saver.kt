package org.persistence.savers

interface Saver<T> {
  fun save(value: T)
}
