package com.kronos.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kronos.core.domain.model.AlertItem
import com.kronos.core.domain.model.AlertSeverity

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey
    val id: String,
    val severity: String,
    val message: String,
    val time: String,
    val server: String,
    val resolved: Boolean
) {
    fun toDomain(): AlertItem {
        return AlertItem(
            id = id,
            severity = AlertSeverity.fromString(severity),
            message = message,
            time = time,
            server = server,
            resolved = resolved
        )
    }

    companion object {
        fun fromDomain(domain: AlertItem): AlertEntity {
            return AlertEntity(
                id = domain.id,
                severity = domain.severity.name,
                message = domain.message,
                time = domain.time,
                server = domain.server,
                resolved = domain.resolved
            )
        }
    }
}
