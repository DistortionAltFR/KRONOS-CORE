package com.kronos.core.data.repository

import com.kronos.core.data.local.dao.AlertDao
import com.kronos.core.data.local.dao.ServerDao
import com.kronos.core.data.local.entity.AlertEntity
import com.kronos.core.data.local.entity.ServerEntity
import com.kronos.core.domain.model.AlertItem
import com.kronos.core.domain.model.AlertSeverity
import com.kronos.core.domain.model.ServerNode
import com.kronos.core.domain.model.ServerStatus
import com.kronos.core.domain.repository.IKronosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class KronosRepository(
    private val serverDao: ServerDao,
    private val alertDao: AlertDao
) : IKronosRepository {

    override fun getServers(): Flow<List<ServerNode>> {
        return serverDao.getAllServers().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAlerts(): Flow<List<AlertItem>> {
        return alertDao.getAllAlerts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertServer(server: ServerNode) = withContext(Dispatchers.IO) {
        serverDao.insertServer(ServerEntity.fromDomain(server))
    }

    override suspend fun deleteServer(serverId: String) = withContext(Dispatchers.IO) {
        serverDao.deleteServerById(serverId)
    }

    override suspend fun resolveAlert(alertId: String) = withContext(Dispatchers.IO) {
        alertDao.resolveAlert(alertId)
    }

    override suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        if (serverDao.getServerCount() == 0) {
            val initialServers = listOf(
                ServerEntity("s1", "PROD-ALPHA-01", "US-EAST", "10.0.1.100", 12, ServerStatus.ONLINE.name, 67, 82, "127d 14h", "Virginia"),
                ServerEntity("s2", "PROD-BETA-02", "EU-WEST", "10.0.2.201", 38, ServerStatus.ONLINE.name, 45, 61, "89d 7h", "Frankfurt"),
                ServerEntity("s3", "STG-GAMMA-03", "US-WEST", "10.0.3.150", 156, ServerStatus.WARNING.name, 92, 95, "12d 3h", "Oregon"),
                ServerEntity("s4", "PROD-DELTA-04", "APAC", "10.0.4.88", 285, ServerStatus.OFFLINE.name, 0, 0, "—", "Singapore"),
                ServerEntity("s5", "DR-EPSILON-05", "US-EAST", "10.0.5.222", 18, ServerStatus.ONLINE.name, 23, 41, "201d 9h", "N. Virginia"),
                ServerEntity("s6", "CACHE-ZETA-06", "EU-WEST", "10.0.6.55", 8, ServerStatus.ONLINE.name, 15, 38, "312d 2h", "Amsterdam")
            )
            serverDao.insertAll(initialServers)
        }

        if (alertDao.getAlertCount() == 0) {
            val initialAlerts = listOf(
                AlertEntity("a1", AlertSeverity.CRITICAL.name, "CPU crítico en STG-GAMMA-03 (92%)", "hace 3m", "STG-GAMMA-03", false),
                AlertEntity("a2", AlertSeverity.CRITICAL.name, "PROD-DELTA-04 sin respuesta — timeout 30s", "hace 12m", "PROD-DELTA-04", false),
                AlertEntity("a3", AlertSeverity.WARNING.name, "Memoria al 95% en STG-GAMMA-03", "hace 38m", "STG-GAMMA-03", false),
                AlertEntity("a4", AlertSeverity.WARNING.name, "Latencia >200ms detectada en APAC", "hace 1h", "PROD-DELTA-04", false),
                AlertEntity("a5", AlertSeverity.INFO.name, "Backup completado en DR-EPSILON-05", "hace 2h", "DR-EPSILON-05", true),
                AlertEntity("a6", AlertSeverity.INFO.name, "Certificado SSL renovado — PROD-BETA-02", "hace 5h", "PROD-BETA-02", true)
            )
            alertDao.insertAll(initialAlerts)
        }
    }
}
