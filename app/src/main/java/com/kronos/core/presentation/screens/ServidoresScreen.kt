package com.kronos.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kronos.core.domain.model.ServerNode
import com.kronos.core.domain.model.ServerStatus
import com.kronos.core.presentation.components.MetricProgressBar
import com.kronos.core.presentation.components.StatusDot
import com.kronos.core.presentation.theme.AcentoAzulNeon
import com.kronos.core.presentation.theme.AdvertenciaAmbar
import com.kronos.core.presentation.theme.AlertaRojo
import com.kronos.core.presentation.theme.BackgroundUltraDark
import com.kronos.core.presentation.theme.BorderSlate700
import com.kronos.core.presentation.theme.CardGrisPizarra
import com.kronos.core.presentation.theme.KronosButtonShape
import com.kronos.core.presentation.theme.KronosCardShape
import com.kronos.core.presentation.theme.KronosChipShape
import com.kronos.core.presentation.theme.SurfaceSlate900
import com.kronos.core.presentation.theme.TextDisabled
import com.kronos.core.presentation.theme.TextMuted
import com.kronos.core.presentation.theme.TextPrimary
import com.kronos.core.presentation.theme.TextSecondary
import com.kronos.core.presentation.theme.VerdeEsmeralda
import com.kronos.core.presentation.viewmodel.KronosUiState

@Composable
fun ServidoresScreen(
    uiState: KronosUiState,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelect: (String) -> Unit,
    onOpenAddServerDialog: () -> Unit,
    onDismissAddServerDialog: () -> Unit,
    onRegisterServer: (name: String, cluster: String, ip: String, sshPort: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterChips = listOf("ALL" to "Todos", "ONLINE" to "Online", "OFFLINE" to "Offline", "WARNING" to "Warning")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundUltraDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Buscador de clusters y servidores
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                placeholder = {
                    Text(
                        text = "Buscar cluster, nodo, región o IP...",
                        color = TextMuted,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = TextSecondary
                    )
                },
                singleLine = true,
                shape = KronosButtonShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardGrisPizarra,
                    unfocusedContainerColor = CardGrisPizarra,
                    focusedBorderColor = AcentoAzulNeon,
                    unfocusedBorderColor = BorderSlate700,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            // Chips de filtrado por estado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                filterChips.forEach { (key, label) ->
                    val isSelected = uiState.selectedStatusFilter.equals(key, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) AcentoAzulNeon else CardGrisPizarra,
                                shape = KronosChipShape
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) AcentoAzulNeon else BorderSlate700,
                                shape = KronosChipShape
                            )
                            .clickable { onFilterSelect(key) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label.uppercase(),
                            color = if (isSelected) BackgroundUltraDark else TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${uiState.filteredServers.size} nodos",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Lista de Nodos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(
                    items = uiState.filteredServers,
                    key = { it.id }
                ) { server ->
                    ServerNodeCard(server = server)
                }
            }
        }

        // Botón '+' para añadir nodo (Floating Action Button)
        FloatingActionButton(
            onClick = onOpenAddServerDialog,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp),
            containerColor = AcentoAzulNeon,
            contentColor = BackgroundUltraDark,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar Nodo",
                modifier = Modifier.size(26.dp)
            )
        }

        // Modal para añadir nuevo nodo
        if (uiState.isAddServerDialogOpen) {
            AddServerDialog(
                onDismiss = onDismissAddServerDialog,
                onConfirm = onRegisterServer
            )
        }
    }
}

@Composable
fun ServerNodeCard(
    server: ServerNode,
    modifier: Modifier = Modifier
) {
    val latencyColor = when {
        server.latency < 50 -> VerdeEsmeralda
        server.latency < 150 -> AdvertenciaAmbar
        else -> AlertaRojo
    }

    val (badgeBg, badgeBorder, badgeText) = when (server.status) {
        ServerStatus.ONLINE -> Triple(VerdeEsmeralda.copy(alpha = 0.15f), VerdeEsmeralda.copy(alpha = 0.4f), VerdeEsmeralda)
        ServerStatus.OFFLINE -> Triple(AlertaRojo.copy(alpha = 0.15f), AlertaRojo.copy(alpha = 0.4f), AlertaRojo)
        ServerStatus.WARNING -> Triple(AdvertenciaAmbar.copy(alpha = 0.15f), AdvertenciaAmbar.copy(alpha = 0.4f), AdvertenciaAmbar)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = KronosCardShape,
        colors = CardDefaults.cardColors(containerColor = CardGrisPizarra),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Encabezado de la tarjeta: Estado dot, Nombre, Región y Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusDot(status = server.status)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = server.name,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${server.cluster} · ${server.region}",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(badgeBg, RoundedCornerShape(8.dp))
                        .border(1.dp, badgeBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = server.status.name,
                        color = badgeText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Barras de CPU y Memoria (si no está offline)
            if (server.status != ServerStatus.OFFLINE) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // CPU bar
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "CPU", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            Text(
                                text = "${server.cpu}%",
                                color = if (server.cpu > 85) AlertaRojo else TextSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        MetricProgressBar(
                            value = server.cpu.toFloat(),
                            barColor = if (server.cpu > 85) AlertaRojo else AcentoAzulNeon
                        )
                    }

                    // MEM bar
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "MEM", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            Text(
                                text = "${server.memory}%",
                                color = if (server.memory > 85) AlertaRojo else TextSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        MetricProgressBar(
                            value = server.memory.toFloat(),
                            barColor = if (server.memory > 85) AlertaRojo else VerdeEsmeralda
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer con latencia, uptime e IP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${server.latency}ms",
                            color = latencyColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = server.uptime,
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Text(
                    text = server.ip,
                    color = TextDisabled,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun AddServerDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, cluster: String, ip: String, sshPort: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var cluster by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("22") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceSlate900, RoundedCornerShape(24.dp))
                .border(1.dp, BorderSlate700, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AGREGAR NODO",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 2.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del nodo (ej. PROD-KUBE-07)", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardGrisPizarra,
                        unfocusedContainerColor = CardGrisPizarra,
                        focusedBorderColor = AcentoAzulNeon,
                        unfocusedBorderColor = BorderSlate700,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = cluster,
                    onValueChange = { cluster = it },
                    label = { Text("Cluster / Grupo (ej. US-EAST)", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardGrisPizarra,
                        unfocusedContainerColor = CardGrisPizarra,
                        focusedBorderColor = AcentoAzulNeon,
                        unfocusedBorderColor = BorderSlate700,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("IP o Hostname (ej. 10.0.1.107)", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardGrisPizarra,
                        unfocusedContainerColor = CardGrisPizarra,
                        focusedBorderColor = AcentoAzulNeon,
                        unfocusedBorderColor = BorderSlate700,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Puerto SSH (22)", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardGrisPizarra,
                        unfocusedContainerColor = CardGrisPizarra,
                        focusedBorderColor = AcentoAzulNeon,
                        unfocusedBorderColor = BorderSlate700,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && ip.isNotBlank()) {
                            onConfirm(name, cluster, ip, port)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = KronosButtonShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AcentoAzulNeon,
                        contentColor = BackgroundUltraDark
                    )
                ) {
                    Text(
                        text = "REGISTRAR NODO",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
