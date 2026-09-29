package com.kronos.core.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    data object Login : Screen("login", "Acceso Seguro")
    data object Dashboard : Screen("dashboard", "KRONOS CORE", Icons.Default.GridView)
    data object Servidores : Screen("servidores", "SERVIDORES", Icons.Default.Dns)
    data object Licencia : Screen("licencia", "LICENCIA", Icons.Default.CreditCard)

    companion object {
        // Pantallas que integran el BottomNavigation persistente
        val bottomNavScreens = listOf(Dashboard, Servidores, Licencia)
    }
}
