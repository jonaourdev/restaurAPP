package com.example.restaurapp.ui.screens.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.DuocBlue
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenBase(
    vm: AuthViewModel,
    isGuestLoading: Boolean,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit,
    horizontalPadding: Dp,
    titleStyle: TextStyle,
    formFieldWithFraction: Float,
    buttonWidthFraction: Float,
    spaceAfterTitle: Dp,
    spaceAfterFields: Dp,
    imageSize: Float,
    spaceAfterImage: Dp
){
    val uiState by vm.uiState.collectAsState()
    val isLoading = uiState.isLoading || isGuestLoading
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                Image(
                    painter = painterResource(id = R.drawable.letras_sin_fondo),
                    contentDescription = "Letras del logo de la app",
                    modifier = Modifier.fillMaxWidth(imageSize)
                )
                Spacer(Modifier.height(spaceAfterImage))
                Text(
                    text = "Iniciar sesión",
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

                // Correo
                OutlinedTextField(
                    value = uiState.loginCorreo,
                    onValueChange = vm::onLoginCorreoChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(formFieldWithFraction),
                    enabled = !isLoading
                )
                Spacer(Modifier.height(8.dp))

                // Contraseña
                OutlinedTextField(
                    value = uiState.loginContrasenna,
                    onValueChange = vm::onLoginContrasennaChange,
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(formFieldWithFraction),
                    enabled = !isLoading
                )

                Spacer(Modifier.height(spaceAfterFields))

                // Botón para llamar a la función de login del ViewModel
                Button(
                    onClick = { vm.login() },
                    modifier = Modifier
                        .fillMaxWidth(buttonWidthFraction)
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    Text("Iniciar sesión")
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onGuestAccess,
                    modifier = Modifier
                        .fillMaxWidth(buttonWidthFraction)
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
                uiState.isLoading -> "Ingresando..."
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
