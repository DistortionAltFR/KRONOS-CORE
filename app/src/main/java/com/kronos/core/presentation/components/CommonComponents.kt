package com.kronos.core.presentation.components

import com.kronos.core.presentation.theme.TextDisabled

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kronos.core.domain.model.AlertItem
import com.kronos.core.domain.model.AlertSeverity
import com.kronos.core.domain.model.ServerStatus
import com.kronos.core.presentation.theme.AcentoAzulNeon
import com.kronos.core.presentation.theme.AdvertenciaAmbar
import com.kronos.core.presentation.theme.AlertaRojo
import com.kronos.core.presentation.theme.BorderSlate700
import com.kronos.core.presentation.theme.CardGrisPizarra
import com.kronos.core.presentation.theme.KronosCardShape
import com.kronos.core.presentation.theme.SurfaceSlate900
import com.kronos.core.presentation.theme.BorderSlate700
import com.kronos.core.presentation.theme.TextMuted
import com.kronos.core.presentation.theme.TextPrimary
import com.kronos.core.presentation.theme.TextSecondary
import com.kronos.core.presentation.theme.VerdeEsmeralda

@Composable
fun LiveBadge(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = modifier
            .background(
                color = VerdeEsmeralda.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            )
            .border(
                width = 1.dp,
                color = VerdeEsmeralda.copy(alpha = 0.35f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(
                    color = VerdeEsmeralda.copy(alpha = alpha),
                    shape = CircleShape
                )
        )
        Text(
            text = "EN VIVO",
            color = VerdeEsmeralda,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun StatusDot(
    status: ServerStatus,
    modifier: Modifier = Modifier
) {
    val color = when (status) {
        ServerStatus.ONLINE -> VerdeEsmeralda
        ServerStatus.OFFLINE -> AlertaRojo
        ServerStatus.WARNING -> AdvertenciaAmbar
    }

    Box(
        modifier = modifier
            .size(10.dp)
            .background(color = color.copy(alpha = 0.3f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color = color, shape = CircleShape)
        )
    }
}

@Composable
fun MetricProgressBar(
    value: Float, // 0..100
    modifier: Modifier = Modifier,
    barColor: Color = AcentoAzulNeon,
    trackColor: Color = Color(0xFF334155).copy(alpha = 0.6f)
) {
    val progress = (value / 100f).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(barColor)
        )
    }
}

data class PerformancePoint(val time: String, val cpu: Int, val traffic: Int)

@Composable
fun PerformanceBarChart(
    modifier: Modifier = Modifier,
    data: List<PerformancePoint> = listOf(
        PerformancePoint("00h", 45, 23),
        PerformancePoint("04h", 32, 18),
        PerformancePoint("08h", 67, 45),
        PerformancePoint("12h", 89, 72),
        PerformancePoint("16h", 76, 58),
        PerformancePoint("20h", 55, 38),
        PerformancePoint("23h", 48, 28)
    )
) {
    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            val width = size.width
            val height = size.height
            val numGroups = data.size
            val groupWidth = width / numGroups
            val barWidth = groupWidth * 0.28f
            val spacing = groupWidth * 0.08f

            // Líneas de cuadrícula horizontal
            val gridLines = 4
            for (i in 0..gridLines) {
                val y = height * (i.toFloat() / gridLines)
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Dibujar barras comparativas
            data.forEachIndexed { index, point ->
                val groupStartX = index * groupWidth + (groupWidth - (barWidth * 2 + spacing)) / 2f

                // Barra CPU (Azul Neón)
                val cpuHeight = (point.cpu / 100f) * height
                val cpuTopY = height - cpuHeight
                drawRoundRect(
                    color = AcentoAzulNeon,
                    topLeft = Offset(groupStartX, cpuTopY),
                    size = Size(barWidth, cpuHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )

                // Barra Tráfico (Verde Esmeralda)
                val trafficStartX = groupStartX + barWidth + spacing
                val trafficHeight = (point.traffic / 100f) * height
                val trafficTopY = height - trafficHeight
                drawRoundRect(
                    color = VerdeEsmeralda,
                    topLeft = Offset(trafficStartX, trafficTopY),
                    size = Size(barWidth, trafficHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Etiquetas del eje X de tiempo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { point ->
                Text(
                    text = point.time,
                    color = TextDisabled,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Leyenda
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(AcentoAzulNeon, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "CPU %",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(VerdeEsmeralda, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Tráfico Red %",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun RangeSliderWithHeader(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    unit: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                color = TextSecondary,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Text(
                text = "${value.toInt()}$unit",
                color = AcentoAzulNeon,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = AcentoAzulNeon,
                activeTrackColor = AcentoAzulNeon,
                inactiveTrackColor = BorderSlate700
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${range.start.toInt()}$unit",
                color = TextMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "${range.endInclusive.toInt()}$unit",
                color = TextMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun AlertsDialog(
    alerts: List<AlertItem>,
    onResolveAlert: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val activeCount = alerts.count { !it.resolved }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardGrisPizarra,
        titleContentColor = TextPrimary,
        shape = KronosCardShape,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = AcentoAzulNeon,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NOTIFICACIONES & ALERTAS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        color = TextPrimary
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (activeCount > 0) "$activeCount alerta(s) activa(s) requieren atención" else "Todas las alertas están resueltas",
                    color = if (activeCount > 0) AlertaRojo else VerdeEsmeralda,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (alerts.isEmpty()) {
                    Text(
                        text = "No hay notificaciones ni alertas registradas.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 340.dp)
                    ) {
                        items(alerts, key = { it.id }) { alert ->
                            AlertItemRow(
                                alert = alert,
                                onResolve = { onResolveAlert(alert.id) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "CERRAR",
                    color = AcentoAzulNeon,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    )
}

@Composable
private fun AlertItemRow(
    alert: AlertItem,
    onResolve: () -> Unit
) {
    val (icon, iconColor) = when (alert.severity) {
        AlertSeverity.CRITICAL -> Icons.Default.Error to AlertaRojo
        AlertSeverity.WARNING -> Icons.Default.Warning to AdvertenciaAmbar
        AlertSeverity.INFO -> Icons.Default.Info to AcentoAzulNeon
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(KronosCardShape)
            .background(SurfaceSlate900)
            .border(1.dp, if (alert.resolved) BorderSlate700 else iconColor.copy(alpha = 0.4f), KronosCardShape)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (alert.resolved) TextMuted else iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = alert.message,
                        color = if (alert.resolved) TextMuted else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 15.sp
                    )
                    Text(
                        text = "${alert.server} · ${alert.time}",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!alert.resolved) {
                Button(
                    onClick = onResolve,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = iconColor.copy(alpha = 0.2f),
                        contentColor = iconColor
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = "Resolver",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Resuelta",
                        tint = VerdeEsmeralda,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Resuelta",
                        color = VerdeEsmeralda,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
