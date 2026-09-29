package com.kronos.core.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Regla de diseño: Esquinas redondeadas estándar de 16dp para Cards y Contenedores
val KronosCardShape = RoundedCornerShape(16.dp)
val KronosButtonShape = RoundedCornerShape(16.dp)
val KronosDialogShape = RoundedCornerShape(24.dp)
val KronosChipShape = RoundedCornerShape(12.dp)

val KronosShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)
