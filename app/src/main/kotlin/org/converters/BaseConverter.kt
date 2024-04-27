package org.converters

abstract class BaseConverter<Left, Right>() {
  abstract fun convertToLeft(right: Right): Left
  abstract fun convertToRight(left: Left): Right
}
