package org.core.validation

interface Validator<T> {
  fun validate(input: T): List<Information>
}
