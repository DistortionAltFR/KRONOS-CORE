package com.kronos.core.presentation.viewmodel

import com.kronos.core.domain.model.AlertItem
import com.kronos.core.domain.model.LicenseCalculation
import com.kronos.core.domain.model.PlanType
import com.kronos.core.domain.model.ServerNode
import com.kronos.core.domain.model.ServerStatus

enum class LicenseStep {
    PLAN_SELECTION,
    RESOURCE_CALCULATOR,
    PAYMENT_CONFIRMATION
}

data class KronosUiState(
    // Autenticación
    val usernameOrEmail: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isAuthenticating: Boolean = false,
    val loginError: String? = null,
    val isAuthenticated: Boolean = false,

    // Métricas en tiempo real
    val cpuUsage: Float = 67f,
    val networkTrafficGb: Float = 4.2f,
    val requestRate: Int = 3847,

    // Servidores & Búsqueda
    val servers: List<ServerNode> = emptyList(),
    val searchQuery: String = "",
    val selectedStatusFilter: String = "ALL", // ALL, ONLINE, OFFLINE, WARNING
    val isAddServerDialogOpen: Boolean = false,
    val selectedServerForDetails: ServerNode? = null,

    // Alertas reactivas
    val alerts: List<AlertItem> = emptyList(),
    val isAlertsDialogOpen: Boolean = false,

    // Licencia & Calculadora
    val selectedPlan: PlanType = PlanType.EMPRESARIAL,
    val vCpus: Int = 8,
    val ramGb: Int = 32,
    val storageGb: Int = 500,
    val licenseStep: LicenseStep = LicenseStep.PLAN_SELECTION,
    val invoiceId: String = ""
) {
    val filteredServers: List<ServerNode>
        get() = servers.filter { server ->
            val matchesQuery = searchQuery.isBlank() ||
                    server.name.contains(searchQuery, ignoreCase = true) ||
                    server.cluster.contains(searchQuery, ignoreCase = true) ||
                    server.region.contains(searchQuery, ignoreCase = true) ||
                    server.ip.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedStatusFilter.uppercase()) {
                "ONLINE" -> server.status == ServerStatus.ONLINE
                "OFFLINE" -> server.status == ServerStatus.OFFLINE
                "WARNING" -> server.status == ServerStatus.WARNING
                else -> true
            }

            matchesQuery && matchesFilter
        }

    val activeAlertsCount: Int
        get() = alerts.count { !it.resolved }

    val licenseCalculation: LicenseCalculation
        get() = LicenseCalculation.calculate(
            plan = selectedPlan,
            vCpus = vCpus,
            ramGb = ramGb,
            storageGb = storageGb
        )
}
