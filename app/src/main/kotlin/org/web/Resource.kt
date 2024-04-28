package org.web

import org.json.JSONObject

class Resource(private val json: String) {
  private val jsonObject = JSONObject(json)

  fun getString(key: String): String {
    return jsonObject.getString(key)
  }
}

class JsonBuilder{
  val json = StringBuilder()

  init{
    json.append("{"))
  }

  fun withPair(key: String, value: String){
    json.append("\"$key:\": \"$value\"")
  }

  fun getJson(): String{
    json.append("}") 
    return json.toString()
  }
}
