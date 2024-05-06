package org.model

import java.time.LocalDateTime

data class AuditLog(
        var id: Long? = null,
        val method: String,
        val path: String,
        val userId: String?,
        val status: Int? = null,
        val time: LocalDateTime? = null
)
