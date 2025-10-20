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
import com.example.restaurapp.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onGoLogin: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val formState by vm.form.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoading = formState.isLoading // Usamos el estado del ViewModel

    // 2. LAUNCHEDEFFECT ELIMINADO
    // Ya no es necesario aquí, porque AppNavHost se encarga de la navegación
    // cuando formState.success es true.

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    // Hacemos la UI de fondo semitransparente mientras carga
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
                formState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Nombre
                OutlinedTextField(
                    value = formState.nombreCompleto,
                    onValueChange = vm::onChangeNombreCompleto,
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    enabled = !isLoading // Deshabilitado mientras carga
                )

                // Correo
                OutlinedTextField(
                    value = formState.correo,
                    onValueChange = vm::onChangeCorreo,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !isLoading // Deshabilitado mientras carga
                )

                // Contraseña
                OutlinedTextField(
                    value = formState.contrasenna,
                    onValueChange = vm::onChangeContrasenna,
                    label = { Text("Contraseña") },
                    // ... resto de propiedades ...
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    enabled = !isLoading // Deshabilitado mientras carga
                )

                // Confirmar contraseña
                OutlinedTextField(
                    value = formState.confirmarContrasenna,
                    onValueChange = vm::onChangeConfirmarContrasenna,
                    label = { Text("Confirmar contraseña") },
                    // ... resto de propiedades ...
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !isLoading // Deshabilitado mientras carga
                )

                Spacer(Modifier.height(24.dp))

                // Botón “Registrarte”
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp),
                    enabled = !isLoading // Deshabilitado mientras carga
                ) {
                    Text("Registrarte")
                }

                // Botón “Cancelar”
                OutlinedButton(
                    onClick = onGoLogin,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    enabled = !isLoading // Deshabilitado mientras carga
                ) {
                    Text("Cancelar")
                }
            }
        }

        // --- 3. CAPA DE CARGA Y MENSAJE DE ÉXITO ---
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
                        text = "¡Registro exitoso!", // El mensaje se muestra durante la carga
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
