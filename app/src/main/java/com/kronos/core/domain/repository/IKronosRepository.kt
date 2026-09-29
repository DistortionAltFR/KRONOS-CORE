package com.kronos.core.domain.repository

import com.kronos.core.domain.model.AlertItem
import com.kronos.core.domain.model.ServerNode
import kotlinx.coroutines.flow.Flow

interface IKronosRepository {
    fun getServers(): Flow<List<ServerNode>>
    fun getAlerts(): Flow<List<AlertItem>>
    suspend fun insertServer(server: ServerNode)
    suspend fun deleteServer(serverId: String)
    suspend fun resolveAlert(alertId: String)
    suspend fun seedInitialDataIfEmpty()
}
