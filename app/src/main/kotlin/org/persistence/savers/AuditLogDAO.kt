package org.persistence.savers

import java.sql.ResultSet
import java.time.Instant
import java.time.temporal.ChronoUnit
import org.model.AuditLog
import org.persistence.H2

class AuditLogDAO(private val database: H2) : Saver<AuditLog> {
    override fun save(value: AuditLog) {
        val id =
                database.insertWithVoid(
                        idQuery = "SELECT NEXT VALUE FOR audit_log_seq",
                        placeHolderQuery =
                                "INSERT INTO audit_log(id, method, path, user_id, status, time) VALUES(?, ?, ?, ?, ?, ?)",
                        value.method,
                        value.path,
                        value.userId,
                        value.status,
                        Instant.now()
                )
        value.id = id
    }

    fun getLogsByHour(hour: Long): List<AuditLog> {
        // TODO Correct time difference between db and here
        val period = Instant.now().minus(10, ChronoUnit.HOURS)
        return database.queryAll(
                placeHolderQuery = "SELECT * from audit_log WHERE time >= ? LIMIT 20",
                rowMapper = ::mapToAuditLog,
                period
        )
    }

    private fun mapToAuditLog(row: ResultSet): AuditLog {
        return AuditLog(
                id = row.getInt("id").toLong(),
                method = row.getString("method"),
                path = row.getString("path"),
                userId = row.getString("user_id"),
                status = row.getInt("status"),
                time = row.getTimestamp("time").toLocalDateTime()
        )
    }
}
