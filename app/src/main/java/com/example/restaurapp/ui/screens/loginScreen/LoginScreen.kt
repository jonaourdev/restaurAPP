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
import com.example.restaurapp.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: LoginViewModel,
    isGuestLoading: Boolean,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit
) {
    val formState by vm.form.collectAsState()
    val isLoading = formState.isLoading || isGuestLoading

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
                // ... (El resto del código de la UI no necesita cambios) ...
                // Text, Spacers, OutlinedTextFields, Buttons... todo se queda igual.
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineLarge,
                    color = DuocBlue,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                formState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = formState.correo,
                    onValueChange = vm::onChangeCorreo,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    enabled = !isLoading
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = formState.contrasenna,
                    onValueChange = vm::onChangeContrasenna,
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
                formState.isLoading -> "¡Login exitoso!"
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
