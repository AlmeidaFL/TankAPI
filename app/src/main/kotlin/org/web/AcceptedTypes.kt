package org.web

enum class MediaTypes(val type: String) {
  JSON("application/json")
}

enum class HttpMethod(val type: String) {
  POST("POST")
}

enum class HttpHeader(val type: String) {
  WWWAUTH("WWW-Authenticate")
}
