package com.kronos.core.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextOverflow
import com.kronos.core.presentation.components.AlertsDialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kronos.core.presentation.components.KronosLogo
import com.kronos.core.presentation.screens.DashboardScreen
import com.kronos.core.presentation.screens.LicenciaScreen
import com.kronos.core.presentation.screens.LoginScreen
import com.kronos.core.presentation.screens.ServidoresScreen
import com.kronos.core.presentation.theme.AcentoAzulNeon
import com.kronos.core.presentation.theme.AlertaRojo
import com.kronos.core.presentation.theme.BackgroundUltraDark
import com.kronos.core.presentation.theme.BorderSlate700
import com.kronos.core.presentation.theme.BorderSlate800
import com.kronos.core.presentation.theme.SurfaceSlate900
import com.kronos.core.presentation.theme.TextMuted
import com.kronos.core.presentation.theme.TextPrimary
import com.kronos.core.presentation.theme.TextSecondary
import com.kronos.core.presentation.viewmodel.KronosUiState
import com.kronos.core.presentation.viewmodel.KronosViewModel
import com.kronos.core.presentation.viewmodel.LicenseStep

@Composable
fun Navegacion(
    navController: NavHostController,
    viewModel: KronosViewModel,
    uiState: KronosUiState,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBars = currentRoute != Screen.Login.route

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundUltraDark,
        topBar = {
            if (shouldShowBars) {
                KronosTopBar(
                    currentRoute = currentRoute,
                    activeAlerts = uiState.activeAlertsCount,
                    onAlertsClick = { viewModel.setAlertsDialogOpen(true) },
                    onLogoutClick = {
                        viewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBars,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                KronosBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigateTo = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isAlertsDialogOpen) {
            AlertsDialog(
                alerts = uiState.alerts,
                onResolveAlert = viewModel::resolveAlert,
                onDismiss = { viewModel.setAlertsDialogOpen(false) }
            )
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. LoginScreen
            composable(Screen.Login.route) {
                LoginScreen(
                    uiState = uiState,
                    onUsernameChange = viewModel::onUsernameChanged,
                    onPasswordChange = viewModel::onPasswordChanged,
                    onTogglePasswordVisibility = viewModel::onPasswordVisibilityToggled,
                    onLoginClick = {
                        viewModel.login {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    },
                    onBiometricClick = {
                        viewModel.loginWithBiometrics {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // 2. DashboardScreen
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    uiState = uiState,
                    onNavigateToLicense = {
                        viewModel.setLicenseStep(LicenseStep.PLAN_SELECTION)
                        navController.navigate(Screen.Licencia.route)
                    }
                )
            }

            // 3. ServidoresScreen
            composable(Screen.Servidores.route) {
                ServidoresScreen(
                    uiState = uiState,
                    onSearchQueryChange = viewModel::onSearchQueryChanged,
                    onFilterSelect = viewModel::onStatusFilterSelected,
                    onOpenAddServerDialog = { viewModel.setAddServerDialogOpen(true) },
                    onDismissAddServerDialog = { viewModel.setAddServerDialogOpen(false) },
                    onRegisterServer = viewModel::addServer
                )
            }

            // 4. LicenciaScreen
            composable(Screen.Licencia.route) {
                LicenciaScreen(
                    uiState = uiState,
                    onPlanSelect = viewModel::onPlanSelected,
                    onVCpusChange = viewModel::onVCpusChanged,
                    onRamChange = viewModel::onRamChanged,
                    onStorageChange = viewModel::onStorageChanged,
                    onNextStep = viewModel::setLicenseStep,
                    onConfirmPayment = viewModel::confirmPayment,
                    onBackToDashboard = {
                        viewModel.setLicenseStep(LicenseStep.PLAN_SELECTION)
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun KronosTopBar(
    currentRoute: String?,
    activeAlerts: Int,
    onAlertsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val title = when (currentRoute) {
        Screen.Dashboard.route -> "KRONOS CORE"
        Screen.Servidores.route -> "SERVIDORES"
        Screen.Licencia.route -> "LICENCIA Y PAGO"
        else -> "KRONOS CORE"
    }

    Surface(
        color = BackgroundUltraDark,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderSlate800)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                KronosLogo(size = 30.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.SansSerif,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Alertas Badge Botón Clickable
                IconButton(onClick = onAlertsClick) {
                    BadgedBox(
                        badge = {
                            if (activeAlerts > 0) {
                                Badge(
                                    containerColor = AlertaRojo,
                                    contentColor = TextPrimary
                                ) {
                                    Text(
                                        text = "$activeAlerts",
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alertas y Notificaciones",
                            tint = if (activeAlerts > 0) AcentoAzulNeon else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Cerrar Sesión
                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar Sesión",
                        tint = AlertaRojo.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun KronosBottomNavigation(
    currentRoute: String?,
    onNavigateTo: (String) -> Unit
) {
    Surface(
        color = BackgroundUltraDark,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderSlate800)
    ) {
        NavigationBar(
            containerColor = BackgroundUltraDark,
            contentColor = TextPrimary,
            tonalElevation = 0.dp,
            windowInsets = NavigationBarDefaults.windowInsets,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Screen.bottomNavScreens.forEach { screen ->
                val selected = currentRoute == screen.route

                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateTo(screen.route) },
                    icon = {
                        screen.icon?.let { icon ->
                            Icon(
                                imageVector = icon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = screen.title,
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AcentoAzulNeon,
                        selectedTextColor = AcentoAzulNeon,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = SurfaceSlate900
                    )
                )
            }
        }
    }
}
