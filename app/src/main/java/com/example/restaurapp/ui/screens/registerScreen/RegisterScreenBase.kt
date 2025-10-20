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
import com.example.restaurapp.viewmodel.RegisterViewModel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenBase(
    vm: RegisterViewModel,
    onGoLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    //Parámetros responsive
    horizontalPadding: Dp,
    titleStyle: TextStyle,
    formFieldWidthFraction: Float,
    buttonWidthFraction: Float,
    spaceAfterTitle: Dp,
    spaceAfterFields: Dp
){
    val formState by vm.form.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoading = formState.isLoading // Usamos el estado del ViewModel


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
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    enabled = !isLoading // Deshabilitado mientras carga
                )

                // Correo
                OutlinedTextField(
                    value = formState.correo,
                    onValueChange = vm::onChangeCorreo,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !isLoading // Deshabilitado mientras carga
                )

                // Contraseña
                OutlinedTextField(
                    value = formState.contrasenna,
                    onValueChange = vm::onChangeContrasenna,
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
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
                    value = formState.confirmarContrasenna,
                    onValueChange = vm::onChangeConfirmarContrasenna,
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(formFieldWidthFraction),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !isLoading
                )

                Spacer(Modifier.height(spaceAfterFields))

                // Botón “Registrarte”
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
                // Botón “Cancelar”
                OutlinedButton(
                    onClick = onGoLogin,
                    modifier = Modifier.fillMaxWidth(buttonWidthFraction),
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
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
                        text = "¡Registro exitoso!", // El mensaje se muestra durante la carga
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

