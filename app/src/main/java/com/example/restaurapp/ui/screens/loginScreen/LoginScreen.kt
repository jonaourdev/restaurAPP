package com.example.restaurapp.ui.screens.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurapp.ui.theme.DuocBlue
// 1. CAMBIAR LA IMPORTACIÓN DEL VIEWMODEL
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: AuthViewModel,
    isGuestLoading: Boolean,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit
) {
    val uiState by vm.uiState.collectAsState()
    val isLoading = uiState.isLoading || isGuestLoading

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 32.dp)
                    .graphicsLayer(alpha = if (isLoading) 0.5f else 1.0f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineLarge,
                    color = DuocBlue,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                // Mostrar el error desde el estado unificado
                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = uiState.loginCorreo,
                    onValueChange = vm::onLoginCorreoChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    enabled = !isLoading
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.loginContrasenna,
                    onValueChange = vm::onLoginContrasennaChange,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    enabled = !isLoading
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { vm.login() },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    Text("Iniciar sesión")
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onGuestAccess,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Entrar como invitado",
                        color = DuocBlue
                    )
                }
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onGoRegister, enabled = !isLoading) {
                    Text(
                        "¿No tienes cuenta? Regístrate aquí",
                        color = DuocBlue
                    )
                }
            }
        }

        if (isLoading) {
            val message = when {
                // El mensaje ahora viene del estado unificado
                uiState.isLoading -> "¡Login exitoso!"
                isGuestLoading -> "Ingresando como invitado..."
                else -> ""
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
