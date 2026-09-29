package com.kronos.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kronos.core.domain.model.ServerNode
import com.kronos.core.domain.model.ServerStatus

@Entity(tableName = "servers")
data class ServerEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val cluster: String,
    val ip: String,
    val latency: Int,
    val status: String,
    val cpu: Int,
    val memory: Int,
    val uptime: String,
    val region: String
) {
    fun toDomain(): ServerNode {
        return ServerNode(
            id = id,
            name = name,
            cluster = cluster,
            ip = ip,
            latency = latency,
            status = ServerStatus.fromString(status),
            cpu = cpu,
            memory = memory,
            uptime = uptime,
            region = region
        )
    }

    companion object {
        fun fromDomain(domain: ServerNode): ServerEntity {
            return ServerEntity(
                id = domain.id,
                name = domain.name,
                cluster = domain.cluster,
                ip = domain.ip,
                latency = domain.latency,
                status = domain.status.name,
                cpu = domain.cpu,
                memory = domain.memory,
                uptime = domain.uptime,
                region = domain.region
            )
        }
    }
}
