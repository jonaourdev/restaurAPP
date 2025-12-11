package com.example.restaurapp.ui.screens.registerScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.example.restaurapp.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenBase(
    vm: AuthViewModel,
    onGoLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    horizontalPadding: Dp,
    titleStyle: TextStyle,
    formFieldWidthFraction: Float,
    buttonWidthFraction: Float,
    spaceAfterTitle: Dp,
    spaceAfterFields: Dp
){
    val uiState by vm.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoading = uiState.isLoading


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = horizontalPadding)
                    .graphicsLayer(alpha = if (isLoading) 0.5f else 1.0f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //  Título
                Text(
                    text = "Crear cuenta",
                    style = titleStyle,
                    color = DuocBlue,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(spaceAfterTitle))

                // Error desde el estado unificado
                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // --- Nombres ---
                OutlinedTextField(
                    value = uiState.registerNombres, // Conectado a 'registerNombres'
                    onValueChange = vm::onRegisterNombresChange, // Conectado a 'onRegisterNombresChange'
                    label = { Text("Nombres") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    enabled = !isLoading
                )

                // --- Apellidos ---
                OutlinedTextField(
                    value = uiState.registerApellidos,
                    onValueChange = vm::onRegisterApellidosChange,
                    label = { Text("Apellidos") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    enabled = !isLoading
                )


                // Correo (sin cambios)
                OutlinedTextField(
                    value = uiState.registerCorreo,
                    onValueChange = vm::onRegisterCorreoChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !isLoading
                )

                // Contraseña (sin cambios)
                OutlinedTextField(
                    value = uiState.registerContrasenna,
                    onValueChange = vm::onRegisterContrasennaChange,
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                        }
                    },
                    enabled = !isLoading
                )

                // Confirmar contraseña (sin cambios)
                OutlinedTextField(
                    value = uiState.registerConfirmarContrasenna,
                    onValueChange = vm::onRegisterConfirmarContrasennaChange,
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !isLoading
                )

                Spacer(Modifier.height(spaceAfterFields))

                // Botón Registrarte (sin cambios)
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth(buttonWidthFraction)
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    Text("Registrarte")
                }
                Spacer(modifier = Modifier.height(6.dp))
                // Botón Cancelar (sin cambios)
                TextButton(onClick = onGoLogin, enabled = !isLoading) {
                    Text(
                        "¿Ya tienes cuenta? Inicia sesión aquí",
                        color = DuocBlue
                    )
                }
            }
        }

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
                        text = "Creando cuenta...",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
