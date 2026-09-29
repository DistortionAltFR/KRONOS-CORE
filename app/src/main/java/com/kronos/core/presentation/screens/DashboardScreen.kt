package com.kronos.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kronos.core.domain.model.AlertSeverity
import com.kronos.core.domain.model.ServerStatus
import com.kronos.core.presentation.components.LiveBadge
import com.kronos.core.presentation.components.MetricProgressBar
import com.kronos.core.presentation.components.PerformanceBarChart
import com.kronos.core.presentation.theme.AcentoAzulNeon
import com.kronos.core.presentation.theme.AlertaRojo
import com.kronos.core.presentation.theme.BackgroundUltraDark
import com.kronos.core.presentation.theme.BorderSlate700
import com.kronos.core.presentation.theme.CardGrisPizarra
import com.kronos.core.presentation.theme.KronosCardShape
import com.kronos.core.presentation.theme.TextDisabled
import com.kronos.core.presentation.theme.TextMuted
import com.kronos.core.presentation.theme.TextPrimary
import com.kronos.core.presentation.theme.TextSecondary
import com.kronos.core.presentation.theme.VerdeEsmeralda
import com.kronos.core.presentation.viewmodel.KronosUiState

@Composable
fun DashboardScreen(
    uiState: KronosUiState,
    onNavigateToLicense: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isCpuCritical = uiState.cpuUsage > 85f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundUltraDark)
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Estado de red 'SISTEMA OPERATIVO'
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(KronosCardShape)
                .background(VerdeEsmeralda.copy(alpha = 0.08f))
                .border(1.dp, VerdeEsmeralda.copy(alpha = 0.35f), KronosCardShape)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(VerdeEsmeralda.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = VerdeEsmeralda,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "SISTEMA OPERATIVO",
                            color = VerdeEsmeralda,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Disponibilidad - SLA Enterprise activo",
                            color = VerdeEsmeralda.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "99.9%",
                    color = VerdeEsmeralda,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        // 2. Encabezado de métricas con badge 'En vivo'
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MÉTRICAS EN TIEMPO REAL",
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                fontFamily = FontFamily.SansSerif
            )
            LiveBadge()
        }

        // 3. Tarjetas de métricas (CPU, Tráfico, Req/s)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Tarjeta CPU
            MetricCard(
                title = "CPU",
                value = "${uiState.cpuUsage.toInt()}",
                unit = "%",
                subText = if (isCpuCritical) "Critico" else "Normal",
                progress = uiState.cpuUsage,
                progressColor = if (isCpuCritical) AlertaRojo else AcentoAzulNeon,
                icon = Icons.Default.Memory,
                modifier = Modifier.weight(1f)
            )

            // Tarjeta Tráfico de Red
            MetricCard(
                title = "RED",
                value = String.format(java.util.Locale.US, "%.1f", uiState.networkTrafficGb),
                unit = "Gb",
                subText = "Trafico/s",
                progress = (uiState.networkTrafficGb * 10f).coerceIn(0f, 100f),
                progressColor = VerdeEsmeralda,
                icon = Icons.Default.NetworkCheck,
                modifier = Modifier.weight(1f)
            )

            // Tarjeta Peticiones (REQ/S)
            MetricCard(
                title = "REQ/S",
                value = String.format(java.util.Locale.US, "%.1f", uiState.requestRate / 1000f),
                unit = "k",
                subText = "Peticiones",
                progress = (uiState.requestRate / 100f).coerceIn(0f, 100f),
                progressColor = AcentoAzulNeon,
                icon = Icons.Default.Speed,
                modifier = Modifier.weight(1f)
            )
        }

        // 4. Gráfico de barras / resumen de rendimiento 24H
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = KronosCardShape,
            colors = CardDefaults.cardColors(containerColor = CardGrisPizarra),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "RENDIMIENTO 24H",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Telemetria CPU y ancho de banda de red",
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                PerformanceBarChart()
            }
        }

        // 5. Estadísticas rápidas: Nodos y Alertas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val totalServers = uiState.servers.size
            val onlineServers = uiState.servers.count { it.status == ServerStatus.ONLINE }
            val warningServers = uiState.servers.count { it.status == ServerStatus.WARNING }
            val offlineServers = uiState.servers.count { it.status == ServerStatus.OFFLINE }

            val totalAlerts = uiState.alerts.count { !it.resolved }
            val criticalAlerts = uiState.alerts.count { !it.resolved && it.severity == AlertSeverity.CRITICAL }
            val warningAlerts = uiState.alerts.count { !it.resolved && it.severity == AlertSeverity.WARNING }

            // Card Nodos
            Card(
                modifier = Modifier.weight(1f),
                shape = KronosCardShape,
                colors = CardDefaults.cardColors(containerColor = CardGrisPizarra),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "NODOS",
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalServers",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = "$onlineServers online - $warningServers warn - $offlineServers off",
                        color = VerdeEsmeralda,
                        fontSize = 8.5.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Card Alertas
            Card(
                modifier = Modifier.weight(1f),
                shape = KronosCardShape,
                colors = CardDefaults.cardColors(containerColor = CardGrisPizarra),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ALERTAS",
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalAlerts",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = "$criticalAlerts criticas - $warningAlerts warnings",
                        color = AlertaRojo,
                        fontSize = 8.5.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // 6. Banner corporativo del Plan / Acceso a Licencia
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(KronosCardShape)
                .background(AcentoAzulNeon.copy(alpha = 0.08f))
                .border(1.dp, AcentoAzulNeon.copy(alpha = 0.35f), KronosCardShape)
                .clickable { onNavigateToLicense() }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = AcentoAzulNeon,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "PLAN EMPRESARIAL ACTIVO",
                            color = AcentoAzulNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Gestionar licencia - Ver facturacion y costos",
                            color = AcentoAzulNeon.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Ir a Licencia",
                    tint = AcentoAzulNeon
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    unit: String,
    subText: String,
    progress: Float,
    progressColor: androidx.compose.ui.graphics.Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = KronosCardShape,
        colors = CardDefaults.cardColors(containerColor = CardGrisPizarra),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AcentoAzulNeon,
                    modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = title,
                    color = TextMuted,
                    fontSize = 8.5.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    maxLines = 1
                )
                Text(
                    text = unit,
                    color = AcentoAzulNeon,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 1.dp, start = 1.dp),
                    maxLines = 1
                )
            }

            Text(
                text = subText,
                color = TextDisabled,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
            )

            MetricProgressBar(
                value = progress,
                barColor = progressColor
            )
        }
    }
}