package com.example.restaurapp.ui.screens.loginScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit // 👈
) {
    val form by vm.form.collectAsState()

    // Si el login es exitoso, redirige
    if (form.success) onLoginSuccess()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DuocBlue,
                    textAlign = TextAlign.Center
                )

                // Mensaje de error
                form.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Campo correo
                OutlinedTextField(
                    value = form.correo,
                    onValueChange = vm::onChangeCorreo,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                // Campo contraseña
                OutlinedTextField(
                    value = form.contrasenna,
                    onValueChange = vm::onChangeContrasenna,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                // Botón iniciar sesión
                Button(
                    onClick = { vm.login() },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                ) {
                    Text("Iniciar sesión")
                }

                // Entrar como invitado
                OutlinedButton(
                    onClick = onGuestAccess,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Entrar como invitado",
                        color = DuocBlue
                    )
                }

                // 🔵 Enlace a registro
                TextButton(onClick = onGoRegister) {
                    Text(
                        "¿No tienes cuenta? Regístrate aquí",
                        color = DuocBlue
                    )
                }
            }
        }
    }
}
