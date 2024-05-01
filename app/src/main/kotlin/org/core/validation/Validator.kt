package org.core.validation

interface Validator<out T> {
  fun validate(input: @UnsafeVariance T)
}
