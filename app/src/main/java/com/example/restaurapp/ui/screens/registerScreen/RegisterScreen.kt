package com.example.restaurapp.ui // O com.example.restaurapp.ui.screens.registerScreen según tu estructura final

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurapp.ui.theme.DuocBlue
// 1. CAMBIAR LA IMPORTACIÓN DEL VIEWMODEL
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onGoLogin: () -> Unit
) {
    val uiState by vm.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoading = uiState.isLoading // Uso del estado del ViewModel unificado


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .graphicsLayer(alpha = if (isLoading) 0.5f else 1.0f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //  Título
                Text(
                    text = "Crear cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DuocBlue,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                // Error
                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Nombre
                OutlinedTextField(
                    value = uiState.registerNombreCompleto,
                    onValueChange = vm::onRegisterNombreChange,
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    enabled = !isLoading
                )

                // Correo
                OutlinedTextField(
                    value = uiState.registerCorreo,
                    onValueChange = vm::onRegisterCorreoChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !isLoading
                )

                // Contraseña
                OutlinedTextField(
                    value = uiState.registerContrasenna,
                    onValueChange = vm::onRegisterContrasennaChange,
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    enabled = !isLoading
                )

                // Confirmar contraseña
                OutlinedTextField(
                    value = uiState.registerConfirmarContrasenna,
                    onValueChange = vm::onRegisterConfirmarContrasennaChange,
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !isLoading
                )

                Spacer(Modifier.height(24.dp))

                // Botón “Registrarte”
                Button(
                    onClick = { vm.registrar() },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    Text("Registrarte")
                }
                Spacer(modifier = Modifier.height(6.dp))
                // Botón “Cancelar”
                OutlinedButton(
                    onClick = onGoLogin,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
                }
            }
        }

        // --- CAPA DE CARGA Y MENSAJE DE ÉXITO ---
        if (isLoading) {
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
                        // El mensaje de éxito ahora se muestra durante el delay de 3 segundos
                        text = "¡Registro exitoso!",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
