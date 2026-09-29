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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kronos.core.domain.model.PlanType
import com.kronos.core.presentation.components.RangeSliderWithHeader
import com.kronos.core.presentation.theme.AcentoAzulNeon
import com.kronos.core.presentation.theme.BackgroundUltraDark
import com.kronos.core.presentation.theme.BorderSlate700
import com.kronos.core.presentation.theme.CardGrisPizarra
import com.kronos.core.presentation.theme.KronosButtonShape
import com.kronos.core.presentation.theme.KronosCardShape
import com.kronos.core.presentation.theme.SurfaceSlate900
import com.kronos.core.presentation.theme.TextDisabled
import com.kronos.core.presentation.theme.TextMuted
import com.kronos.core.presentation.theme.TextPrimary
import com.kronos.core.presentation.theme.TextSecondary
import com.kronos.core.presentation.theme.VerdeEsmeralda
import com.kronos.core.presentation.viewmodel.KronosUiState
import com.kronos.core.presentation.viewmodel.LicenseStep
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LicenciaScreen(
    uiState: KronosUiState,
    onPlanSelect: (PlanType) -> Unit,
    onVCpusChange: (Int) -> Unit,
    onRamChange: (Int) -> Unit,
    onStorageChange: (Int) -> Unit,
    onNextStep: (LicenseStep) -> Unit,
    onConfirmPayment: () -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundUltraDark)
    ) {
        when (uiState.licenseStep) {
            LicenseStep.PLAN_SELECTION -> {
                PlanSelectionView(
                    selectedPlan = uiState.selectedPlan,
                    onPlanSelect = onPlanSelect,
                    onProceedToCalculator = { onNextStep(LicenseStep.RESOURCE_CALCULATOR) }
                )
            }
            LicenseStep.RESOURCE_CALCULATOR -> {
                ResourceCalculatorView(
                    uiState = uiState,
                    onVCpusChange = onVCpusChange,
                    onRamChange = onRamChange,
                    onStorageChange = onStorageChange,
                    onBackToPlans = { onNextStep(LicenseStep.PLAN_SELECTION) },
                    onProceedToConfirm = onConfirmPayment
                )
            }
            LicenseStep.PAYMENT_CONFIRMATION -> {
                PaymentConfirmationView(
                    uiState = uiState,
                    onBackToDashboard = onBackToDashboard
                )
            }
        }
    }
}

@Composable
private fun PlanSelectionView(
    selectedPlan: PlanType,
    onPlanSelect: (PlanType) -> Unit,
    onProceedToCalculator: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "PLANES CORPORATIVOS",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.SansSerif
        )

        Text(
            text = "Selecciona la infraestructura cloud que mejor se adapta a tu centro de datos y escala operativa.",
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        // Cards de planes
        PlanType.entries.forEach { plan ->
            val isSelected = selectedPlan == plan
            val borderColor = if (isSelected) AcentoAzulNeon else BorderSlate700
            val containerColor = if (isSelected) AcentoAzulNeon.copy(alpha = 0.08f) else CardGrisPizarra

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlanSelect(plan) },
                shape = KronosCardShape,
                colors = CardDefaults.cardColors(containerColor = containerColor),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(borderColor)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = plan.title.uppercase(),
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                                if (plan == PlanType.DEDICADO) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(AcentoAzulNeon.copy(alpha = 0.2f), RoundedCornerShape(50))
                                            .border(1.dp, AcentoAzulNeon.copy(alpha = 0.5f), RoundedCornerShape(50))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "PREMIUM",
                                            color = AcentoAzulNeon,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Text(
                                text = plan.description,
                                color = TextMuted,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Radio selector personalizado
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .border(2.dp, if (isSelected) AcentoAzulNeon else BorderSlate700, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(AcentoAzulNeon, CircleShape)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$${plan.basePrice}",
                            color = AcentoAzulNeon,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = "/mes base",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Características
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        plan.features.forEach { feature ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (isSelected) AcentoAzulNeon else TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = feature,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onProceedToCalculator,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = KronosButtonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AcentoAzulNeon,
                contentColor = BackgroundUltraDark
            )
        ) {
            Text(
                text = "CONFIGURAR RECURSOS →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun ResourceCalculatorView(
    uiState: KronosUiState,
    onVCpusChange: (Int) -> Unit,
    onRamChange: (Int) -> Unit,
    onStorageChange: (Int) -> Unit,
    onBackToPlans: () -> Unit,
    onProceedToConfirm: () -> Unit
) {
    val scrollState = rememberScrollState()
    val plan = uiState.selectedPlan
    val calc = uiState.licenseCalculation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackToPlans) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = TextPrimary
                )
            }
            Text(
                text = "CALCULADORA DE RECURSOS",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        // Resumen del plan seleccionado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(KronosCardShape)
                .background(AcentoAzulNeon.copy(alpha = 0.08f))
                .border(1.dp, AcentoAzulNeon.copy(alpha = 0.35f), KronosCardShape)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PLAN ${plan.title.uppercase()}",
                        color = AcentoAzulNeon,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Base de suscripción: $${plan.basePrice}/mes",
                        color = AcentoAzulNeon.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = AcentoAzulNeon
                )
            }
        }

        // Sliders en tiempo real
        RangeSliderWithHeader(
            label = "vCPUs Virtuales",
            value = uiState.vCpus.toFloat(),
            range = 2f..plan.maxCpu.toFloat(),
            unit = " vCPU",
            onValueChange = { onVCpusChange(it.toInt()) }
        )

        RangeSliderWithHeader(
            label = "Memoria RAM",
            value = uiState.ramGb.toFloat(),
            range = 4f..plan.maxRam.toFloat(),
            unit = " GB",
            onValueChange = { onRamChange(it.toInt()) }
        )

        RangeSliderWithHeader(
            label = "Almacenamiento NVMe",
            value = uiState.storageGb.toFloat(),
            range = 100f..plan.maxStorage.toFloat(),
            unit = " GB",
            onValueChange = { onStorageChange(it.toInt()) }
        )

        // Tarjeta con desglose de costos en tiempo real
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = KronosCardShape,
            colors = CardDefaults.cardColors(containerColor = CardGrisPizarra),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DESGLOSE DE COSTOS",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                CostRow(label = "Plan ${plan.title} (Base)", value = "$${calc.baseCost}")
                CostRow(label = "${uiState.vCpus} vCPUs × $8/u", value = "$${calc.cpuCost}")
                CostRow(label = "${uiState.ramGb}GB RAM × $2/GB", value = "$${calc.ramCost}")
                CostRow(label = "${uiState.storageGb}GB × $0.05/GB", value = "$${calc.storageCost}")

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderSlate700)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL MENSUAL",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.sp
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$${calc.totalCost}",
                            color = AcentoAzulNeon,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = "/mes",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                        )
                    }
                }
            }
        }

        // Botón de confirmación de pago
        Button(
            onClick = onProceedToConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = KronosButtonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AcentoAzulNeon,
                contentColor = BackgroundUltraDark
            )
        ) {
            Text(
                text = "CONFIRMAR PAGO · $${calc.totalCost}/mes",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun PaymentConfirmationView(
    uiState: KronosUiState,
    onBackToDashboard: () -> Unit
) {
    val scrollState = rememberScrollState()
    val calc = uiState.licenseCalculation
    val currentDate = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "ES")).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Badge de éxito
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(VerdeEsmeralda.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                .border(1.dp, VerdeEsmeralda.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Pago Exitoso",
                tint = VerdeEsmeralda,
                modifier = Modifier.size(38.dp)
            )
        }

        Text(
            text = "PAGO CONFIRMADO",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.SansSerif
        )

        Text(
            text = "TRANSACCIÓN CIFRADA · AES-256-GCM",
            color = VerdeEsmeralda,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // Comprobante / Factura Oficial
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = KronosCardShape,
            colors = CardDefaults.cardColors(containerColor = CardGrisPizarra),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
            )
        ) {
            Column {
                // Header Factura
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceSlate900)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "FACTURA KRONOS CORE",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = currentDate,
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Seguro",
                        tint = VerdeEsmeralda,
                        modifier = Modifier.size(16.dp)
                    )
                }

                HorizontalDivider(color = BorderSlate700)

                // Detalles
                Column(modifier = Modifier.padding(16.dp)) {
                    CostRow(label = "Plan ${uiState.selectedPlan.title}", value = "$${calc.baseCost}")
                    CostRow(label = "${uiState.vCpus} vCPUs asignadas", value = "$${calc.cpuCost}")
                    CostRow(label = "${uiState.ramGb} GB RAM dedicada", value = "$${calc.ramCost}")
                    CostRow(label = "${uiState.storageGb} GB NVMe Enterprise", value = "$${calc.storageCost}")

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderSlate700)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL / MES",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = "$${calc.totalCost}",
                            color = AcentoAzulNeon,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }

                HorizontalDivider(color = BorderSlate700)

                // ID de transacción
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceSlate900)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "ID DE TRANSACCIÓN CIFRADA",
                        color = TextDisabled,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = uiState.invoiceId,
                        color = AcentoAzulNeon,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Garantía de seguridad PCI DSS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeEsmeralda.copy(alpha = 0.06f), KronosCardShape)
                .border(1.dp, VerdeEsmeralda.copy(alpha = 0.25f), KronosCardShape)
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = VerdeEsmeralda,
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Transacción verificada y cifrada con AES-256-GCM. Datos procesados bajo estándar corporativo PCI DSS Level 1 con token de factura seguro.",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBackToDashboard,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = KronosButtonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AcentoAzulNeon,
                contentColor = BackgroundUltraDark
            )
        ) {
            Text(
                text = "VOLVER AL DASHBOARD",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.5.sp
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun CostRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        Text(text = value, color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
    }
}
