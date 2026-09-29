package com.kronos.core.presentation.screens

import com.kronos.core.presentation.theme.TextDisabled

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kronos.core.presentation.components.KronosLogo
import com.kronos.core.presentation.theme.AcentoAzulNeon
import com.kronos.core.presentation.theme.AlertaRojo
import com.kronos.core.presentation.theme.BackgroundUltraDark
import com.kronos.core.presentation.theme.BorderSlate700
import com.kronos.core.presentation.theme.CardGrisPizarra
import com.kronos.core.presentation.theme.KronosButtonShape
import com.kronos.core.presentation.theme.SurfaceSlate900
import com.kronos.core.presentation.theme.TextMuted
import com.kronos.core.presentation.theme.TextPrimary
import com.kronos.core.presentation.theme.TextSecondary
import com.kronos.core.presentation.theme.VerdeEsmeralda
import com.kronos.core.presentation.viewmodel.KronosUiState

@Composable
fun LoginScreen(
    uiState: KronosUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onBiometricClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundUltraDark)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo y Encabezado de la Plataforma
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(SurfaceSlate900, RoundedCornerShape(24.dp))
                    .border(1.dp, BorderSlate700, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                KronosLogo(size = 64.dp)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "KRONOS",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = "CORE PLATFORM",
                color = AcentoAzulNeon,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Badge de seguridad
            Row(
                modifier = Modifier
                    .background(VerdeEsmeralda.copy(alpha = 0.12f), RoundedCornerShape(50))
                    .border(1.dp, VerdeEsmeralda.copy(alpha = 0.3f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = VerdeEsmeralda,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ACCESO SEGURO · V3.2.1",
                    color = VerdeEsmeralda,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Banner de validación de error en rojo (#F43F5E) si hay campos vacíos o inválidos
            AnimatedVisibility(
                visible = uiState.loginError != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                uiState.loginError?.let { errorMsg ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .background(AlertaRojo.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                            .border(1.dp, AlertaRojo.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = AlertaRojo,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = errorMsg,
                            color = AlertaRojo,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Campo Usuario / Correo
            Text(
                text = "CORREO ELECTRÓNICO CORPORATIVO",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp, start = 4.dp)
            )

            val isEmailError = uiState.loginError?.contains("correo", ignoreCase = true) == true

            OutlinedTextField(
                value = uiState.usernameOrEmail,
                onValueChange = onUsernameChange,
                modifier = Modifier.fillMaxWidth(),
                isError = isEmailError,
                placeholder = {
                    Text(
                        text = "admin@kronos-enterprise.io",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = if (isEmailError) AlertaRojo else TextSecondary
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
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AcentoAzulNeon,
                    errorBorderColor = AlertaRojo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            Text(
                text = "CONTRASEÑA (MÍNIMO 6 CARACTERES)",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp, start = 4.dp)
            )

            val isPasswordError = uiState.loginError?.contains("contraseña", ignoreCase = true) == true

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                isError = isPasswordError,
                placeholder = {
                    Text(
                        text = "••••••••••••",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isPasswordError) AlertaRojo else TextSecondary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Mostrar u ocultar contraseña",
                            tint = TextSecondary
                        )
                    }
                },
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = KronosButtonShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardGrisPizarra,
                    unfocusedContainerColor = CardGrisPizarra,
                    focusedBorderColor = AcentoAzulNeon,
                    unfocusedBorderColor = BorderSlate700,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AcentoAzulNeon,
                    errorBorderColor = AlertaRojo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onLoginClick()
                    }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón 'Iniciar Sesión' Azul Neón (#38BDF8)
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onLoginClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = KronosButtonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AcentoAzulNeon,
                    contentColor = BackgroundUltraDark
                ),
                enabled = !uiState.isAuthenticating
            ) {
                if (uiState.isAuthenticating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = BackgroundUltraDark,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "AUTENTICANDO...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.sp
                    )
                } else {
                    Text(
                        text = "INICIAR SESIÓN",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divisor "o usa biometría"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = BorderSlate700
                )
                Text(
                    text = "o usa biometría",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = BorderSlate700
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de autenticación biométrica
            OutlinedButton(
                onClick = onBiometricClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = KronosButtonShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CardGrisPizarra,
                    contentColor = TextPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(BorderSlate700)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Huella",
                    tint = AcentoAzulNeon,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Autenticación Biométrica",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Pie de página de seguridad corporativa
            Text(
                text = "Kronos Core · Enterprise Security · Cifrado AES-256-GCM",
                color = TextDisabled,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        }
    }
}
