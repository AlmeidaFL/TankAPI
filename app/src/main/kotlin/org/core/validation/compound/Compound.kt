package org.core.validation.compound

import kotlin.collections.mutableListOf
import org.core.validation.Validator

class Compound<T> {
  private val validators = mutableListOf<Validator<T>>()

  fun validate(input: T) {
    validators.forEach { it.validate(input) }
  }

  fun addValidator(validator: Validator<T>) {
    validators.add(validator)
  }
}
