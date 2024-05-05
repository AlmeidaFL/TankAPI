package org.converters

import org.json.JSONArray
import org.json.JSONObject
import org.model.AuditLog

class AuditLogConverter {
    companion object{
        fun convertToJson(logs: List<AuditLog>): JSONArray{
            val jsonLogs = mutableListOf<JSONObject>()
            logs.forEach {
                val log = JSONObject()
                    .put("method", it.method)
                    .put("path", it.path)
                    .put("user_id", it.userId)
                    .put("time", it.time)
                    .put("request_status", it.status)
                jsonLogs.add(log)
            }
            return JSONArray(jsonLogs)
        }
    }
}