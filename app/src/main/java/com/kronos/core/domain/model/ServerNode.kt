package com.kronos.core.domain.model

enum class ServerStatus {
    ONLINE,
    OFFLINE,
    WARNING;

    companion object {
        fun fromString(value: String): ServerStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: ONLINE
        }
    }
}

data class ServerNode(
    val id: String,
    val name: String,
    val cluster: String,
    val ip: String,
    val latency: Int, // ms
    val status: ServerStatus,
    val cpu: Int, // %
    val memory: Int, // %
    val uptime: String,
    val region: String
)
