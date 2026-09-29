package com.kronos.core.domain.model

enum class AlertSeverity {
    CRITICAL,
    WARNING,
    INFO;

    companion object {
        fun fromString(value: String): AlertSeverity {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: INFO
        }
    }
}

data class AlertItem(
    val id: String,
    val severity: AlertSeverity,
    val message: String,
    val time: String,
    val server: String,
    val resolved: Boolean = false
)
