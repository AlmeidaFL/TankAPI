package org.controller

import org.controller.dto.RequestDTO
import org.controller.dto.ResponseDTO
import org.converters.AuditLogConverter
import org.core.services.AuditLogService
import org.core.services.Time
import org.json.JSONArray
import org.model.AuditLog
import spark.Request
import spark.Response

class AuditController(private val service: AuditLogService) {

  fun logRequestEntry(request: Request, response: Response) {
    val requestDto =
        RequestDTO(request.requestMethod(), request.pathInfo(), request.attribute("subject"))
    service.auditStart(requestDto)
  }

  fun logRequestExit(request: Request, response: Response) {
    val requestDto =
        RequestDTO(request.requestMethod(), request.pathInfo(), request.attribute("subject"))
    val responseDto = ResponseDTO(response.status())
    service.auditEnd(requestDto, responseDto)
  }

  fun getLogsByHour(request: Request, response: Response): JSONArray{
    val period = request.queryParams("period")?.toLong() ?: 1
    return AuditLogConverter.convertToJson(service.getLogsBy(period, Time.HOUR))
  }
}
