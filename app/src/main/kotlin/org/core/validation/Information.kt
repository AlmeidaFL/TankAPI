package org.core.validation

data class Information(val message: String, val infoType: InfoType)

enum class InfoType {
    ERROR,
    INFO
}
