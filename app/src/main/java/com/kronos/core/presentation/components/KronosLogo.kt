package com.kronos.core.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kronos.core.presentation.theme.AcentoAzulNeon
import com.kronos.core.presentation.theme.VerdeEsmeralda
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun KronosLogo(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Canvas(modifier = modifier.size(size)) {
        val width = this.size.width
        val height = this.size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width * 0.40f

        // 1. Hexágono exterior corporativo
        val hexPath = Path().apply {
            for (i in 0 until 6) {
                val angle = Math.toRadians((60 * i - 30).toDouble())
                val x = centerX + radius * cos(angle).toFloat()
                val y = centerY + radius * sin(angle).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }

        drawPath(
            path = hexPath,
            color = Color(0xFF0F172A)
        )
        drawPath(
            path = hexPath,
            color = Color(0xFF334155),
            style = Stroke(width = width * 0.035f)
        )

        // 2. Líneas de circuito interior (KRONOS "K" geométrica)
        val beamBrush = Brush.linearGradient(
            colors = listOf(AcentoAzulNeon, VerdeEsmeralda),
            start = Offset(0f, 0f),
            end = Offset(width, height)
        )

        val strokeWidth = width * 0.08f
        val colX = width * 0.32f
        val topY = height * 0.28f
        val bottomY = height * 0.72f
        val midY = height * 0.50f
        val branchRightX = width * 0.72f

        // Barra vertical izquierda
        drawLine(
            brush = beamBrush,
            start = Offset(colX, topY),
            end = Offset(colX, bottomY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Rama diagonal superior
        drawLine(
            brush = beamBrush,
            start = Offset(colX, midY),
            end = Offset(branchRightX, topY),
            strokeWidth = strokeWidth * 0.85f,
            cap = StrokeCap.Round
        )

        // Rama diagonal inferior
        drawLine(
            brush = beamBrush,
            start = Offset(colX, midY),
            end = Offset(branchRightX, bottomY),
            strokeWidth = strokeWidth * 0.85f,
            cap = StrokeCap.Round
        )

        // Nodos luminosos con brillo
        val nodeRadius = width * 0.06f
        drawCircle(
            color = AcentoAzulNeon.copy(alpha = pulseAlpha),
            radius = nodeRadius * 1.3f,
            center = Offset(colX, topY)
        )
        drawCircle(
            color = AcentoAzulNeon,
            radius = nodeRadius,
            center = Offset(colX, topY)
        )

        drawCircle(
            color = AcentoAzulNeon.copy(alpha = pulseAlpha),
            radius = nodeRadius * 1.3f,
            center = Offset(colX, bottomY)
        )
        drawCircle(
            color = AcentoAzulNeon,
            radius = nodeRadius,
            center = Offset(colX, bottomY)
        )

        drawCircle(
            color = VerdeEsmeralda.copy(alpha = pulseAlpha),
            radius = nodeRadius * 1.3f,
            center = Offset(branchRightX, topY)
        )
        drawCircle(
            color = VerdeEsmeralda,
            radius = nodeRadius,
            center = Offset(branchRightX, topY)
        )

        drawCircle(
            color = VerdeEsmeralda.copy(alpha = pulseAlpha),
            radius = nodeRadius * 1.3f,
            center = Offset(branchRightX, bottomY)
        )
        drawCircle(
            color = VerdeEsmeralda,
            radius = nodeRadius,
            center = Offset(branchRightX, bottomY)
        )
    }
}
