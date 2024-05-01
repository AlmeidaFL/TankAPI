package org.web

import org.json.JSONObject

class Resource(private val json: String) {
  private val jsonObject = JSONObject(json)

  fun getString(key: String): String {
    return jsonObject.getString(key)
  }
}
