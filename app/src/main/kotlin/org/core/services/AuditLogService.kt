package org.core.services

import org.controller.dto.RequestDTO
import org.controller.dto.ResponseDTO
import org.model.AuditLog
import org.persistence.savers.AuditLogDAO

class AuditLogService(private val auditDao: AuditLogDAO) {
  public fun auditStart(request: RequestDTO) {
    val audit = AuditLog(path = request.pathInfo, method = request.method, userId = request.userId)
    auditDao.save(audit)
  }

  public fun auditEnd(request: RequestDTO, response: ResponseDTO) {
    val audit =
        AuditLog(
            path = request.pathInfo,
            status = response.status,
            method = request.method,
            userId = request.userId
        )
    auditDao.save(audit)
  }

  fun getLogsBy(time: Long, timeType: Time): List<AuditLog>{
    when (timeType) {
      Time.HOUR -> return auditDao.getLogsByHour(time)
    }
  }
}

enum class Time {
  HOUR,
}
