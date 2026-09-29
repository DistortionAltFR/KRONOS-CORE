package com.kronos.core.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KronosDarkColorScheme = darkColorScheme(
    primary = AcentoAzulNeon,
    onPrimary = BackgroundUltraDark,
    primaryContainer = CardGrisPizarra,
    onPrimaryContainer = AcentoAzulNeon,

    secondary = VerdeEsmeralda,
    onSecondary = BackgroundUltraDark,
    secondaryContainer = CardGrisPizarra,
    onSecondaryContainer = VerdeEsmeralda,

    tertiary = AdvertenciaAmbar,
    onTertiary = BackgroundUltraDark,

    background = BackgroundUltraDark,
    onBackground = TextPrimary,

    surface = CardGrisPizarra,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceSlate900,
    onSurfaceVariant = TextSecondary,

    error = AlertaRojo,
    onError = TextPrimary,
    errorContainer = CardGrisPizarra,
    onErrorContainer = AlertaRojo,

    outline = BorderSlate700,
    outlineVariant = BorderSlate800
)

@Composable
fun KronosTheme(
    darkTheme: Boolean = true, // Siempre Dark Mode Corporativo por especificación
    content: @Composable () -> Unit
) {
    val colorScheme = KronosDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BackgroundUltraDark.toArgb()
            window.navigationBarColor = BackgroundUltraDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KronosTypography,
        shapes = KronosShapes,
        content = content
    )
}
