package com.example.restaurapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.screens.navBarScreen.NavBarScreen
import com.example.restaurapp.ui.theme.DuocBlue
import com.example.restaurapp.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    navController: NavController
) {
    val form by vm.form.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val currentRoute = navController.currentDestination?.route

    // Si el registro fue exitoso, esperar 3s y redirigir al login
    LaunchedEffect(form.success) {
        if (form.success) {
            delay(3000)
            vm.limpiarFormulario()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }
    }

    Scaffold{ padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //  Título
                Text(
                    text = "Crear cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DuocBlue,
                    textAlign = TextAlign.Center
                )

                // Error
                form.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Mensaje de éxito
                if (form.success) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = DuocBlue,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = " ✔ Registro exitoso. Redirigiendo...",
                            color = DuocBlue,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Nombre
                OutlinedTextField(
                    value = form.nombreCompleto,
                    onValueChange = vm::onChangeNombreCompleto,
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    enabled = !form.success
                )

                // Correo
                OutlinedTextField(
                    value = form.correo,
                    onValueChange = vm::onChangeCorreo,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !form.success
                )

                // Contraseña
                OutlinedTextField(
                    value = form.contrasenna,
                    onValueChange = vm::onChangeContrasenna,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        val description =
                            if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    },
                    enabled = !form.success
                )

                // Confirmar contraseña
                OutlinedTextField(
                    value = form.confirmarContrasenna,
                    onValueChange = vm::onChangeConfirmarContrasenna,
                    label = { Text("Confirmar contraseña") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !form.success
                )

                // Botón “Registrarte”
                Button(
                    onClick = onSaved,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp),
                    enabled = !form.success
                ) {
                    Text("Registrarte")
                }

                // Botón “Cancelar”
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    enabled = !form.success
                ) {
                    Text("Cancelar")
                }

                // Ir a Login
                TextButton(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    enabled = !form.success
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta? Inicia sesión",
                        color = DuocBlue
                    )
                }
            }
        }
    }
}
