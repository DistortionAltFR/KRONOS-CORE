package com.kronos.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kronos.core.domain.model.PlanType
import com.kronos.core.domain.model.ServerNode
import com.kronos.core.domain.model.ServerStatus
import com.kronos.core.domain.repository.IKronosRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class KronosViewModel(
    private val repository: IKronosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KronosUiState())
    val uiState: StateFlow<KronosUiState> = _uiState.asStateFlow()

    init {
        // Inicializar datos semilla si la base de datos está vacía
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        // Suscribirse a los flujos reactivos de Room
        observeServers()
        observeAlerts()

        // Iniciar telemetría en vivo simulada
        startLiveTelemetry()
    }

    private fun observeServers() {
        viewModelScope.launch {
            repository.getServers().collect { serverList ->
                _uiState.update { it.copy(servers = serverList) }
            }
        }
    }

    private fun observeAlerts() {
        viewModelScope.launch {
            repository.getAlerts().collect { alertList ->
                _uiState.update { it.copy(alerts = alertList) }
            }
        }
    }

    private fun startLiveTelemetry() {
        viewModelScope.launch {
            while (isActive) {
                delay(2800)
                _uiState.update { current ->
                    val cpuDelta = (Random.nextFloat() - 0.48f) * 8f
                    val newCpu = (current.cpuUsage + cpuDelta).coerceIn(20f, 98f)

                    val trafficDelta = (Random.nextFloat() - 0.5f) * 0.8f
                    val newTraffic = (current.networkTrafficGb + trafficDelta).coerceIn(0.5f, 9.9f)

                    val reqDelta = Random.nextInt(-250, 300)
                    val newReq = (current.requestRate + reqDelta).coerceIn(500, 9999)

                    current.copy(
                        cpuUsage = newCpu,
                        networkTrafficGb = newTraffic,
                        requestRate = newReq
                    )
                }
            }
        }
    }

    // --- ACCIONES DE AUTENTICACIÓN ---
    fun onUsernameChanged(value: String) {
        _uiState.update { it.copy(usernameOrEmail = value, loginError = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, loginError = null) }
    }

    fun onPasswordVisibilityToggled() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login(onSuccess: () -> Unit) {
        val current = _uiState.value
        val email = current.usernameOrEmail.trim()
        val password = current.password

        if (email.isBlank()) {
            _uiState.update {
                it.copy(loginError = "Error: El correo electrónico es obligatorio.")
            }
            return
        }

        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (!emailRegex.matches(email)) {
            _uiState.update {
                it.copy(loginError = "Error: Ingrese un correo electrónico válido (ej. usuario@dominio.com).")
            }
            return
        }

        if (password.isBlank()) {
            _uiState.update {
                it.copy(loginError = "Error: La contraseña es obligatoria.")
            }
            return
        }

        if (password.length < 6) {
            _uiState.update {
                it.copy(loginError = "Error: La contraseña debe tener al menos 6 caracteres.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAuthenticating = true, loginError = null) }
            delay(1200) // Simulación de cifrado TLS y handshake
            _uiState.update {
                it.copy(isAuthenticating = false, isAuthenticated = true, loginError = null)
            }
            onSuccess()
        }
    }

    fun loginWithBiometrics(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthenticating = true, loginError = null) }
            delay(1000)
            _uiState.update {
                it.copy(
                    isAuthenticating = false,
                    isAuthenticated = true,
                    usernameOrEmail = "admin@kronos-enterprise.io",
                    loginError = null
                )
            }
            onSuccess()
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        _uiState.update {
            it.copy(
                isAuthenticated = false,
                password = "",
                loginError = null
            )
        }
        onLoggedOut()
    }

    // --- ACCIONES DE SERVIDORES ---
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onStatusFilterSelected(filter: String) {
        _uiState.update { it.copy(selectedStatusFilter = filter) }
    }

    fun setAddServerDialogOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isAddServerDialogOpen = isOpen) }
    }

    fun addServer(name: String, cluster: String, ip: String, sshPort: String) {
        if (name.isBlank() || ip.isBlank()) return

        val newServer = ServerNode(
            id = "node-${UUID.randomUUID().toString().take(8)}",
            name = name.trim().uppercase(),
            cluster = if (cluster.isBlank()) "GENERAL" else cluster.trim().uppercase(),
            ip = ip.trim(),
            latency = Random.nextInt(5, 120),
            status = ServerStatus.ONLINE,
            cpu = Random.nextInt(15, 60),
            memory = Random.nextInt(20, 70),
            uptime = "0d 1h",
            region = "Global Cloud"
        )

        viewModelScope.launch {
            repository.insertServer(newServer)
            _uiState.update { it.copy(isAddServerDialogOpen = false) }
        }
    }

    fun deleteServer(id: String) {
        viewModelScope.launch {
            repository.deleteServer(id)
        }
    }

    // --- ACCIONES DE ALERTAS ---
    fun setAlertsDialogOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isAlertsDialogOpen = isOpen) }
    }

    fun resolveAlert(id: String) {
        viewModelScope.launch {
            repository.resolveAlert(id)
        }
    }

    // --- ACCIONES DE LICENCIA & FACTURACIÓN ---
    fun onPlanSelected(plan: PlanType) {
        _uiState.update { current ->
            current.copy(
                selectedPlan = plan,
                vCpus = if (plan == PlanType.EMPRESARIAL) 8 else 32,
                ramGb = if (plan == PlanType.EMPRESARIAL) 32 else 128,
                storageGb = if (plan == PlanType.EMPRESARIAL) 500 else 2000
            )
        }
    }

    fun onVCpusChanged(vCpus: Int) {
        _uiState.update { it.copy(vCpus = vCpus) }
    }

    fun onRamChanged(ramGb: Int) {
        _uiState.update { it.copy(ramGb = ramGb) }
    }

    fun onStorageChanged(storageGb: Int) {
        _uiState.update { it.copy(storageGb = storageGb) }
    }

    fun setLicenseStep(step: LicenseStep) {
        _uiState.update { it.copy(licenseStep = step) }
    }

    fun confirmPayment() {
        val invoice = "KRN-${System.currentTimeMillis().toString(36).uppercase().takeLast(6)}-${UUID.randomUUID().toString().take(6).uppercase()}"
        _uiState.update {
            it.copy(
                licenseStep = LicenseStep.PAYMENT_CONFIRMATION,
                invoiceId = invoice
            )
        }
    }
}
