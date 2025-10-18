package com.example.restaurapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.RegisterViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {

    val form by vm.form.collectAsState()
    val passwordVisible = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") })
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            form.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            OutlinedTextField(
                value = form.nombreCompleto,
                onValueChange = vm::onChangeNombreCompleto,
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.correo,
                onValueChange = vm::onChangeCorreo,
                label = { Text("Correo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: Contraseña (oculta por defecto)
            OutlinedTextField(
                value = form.contrasenna,
                onValueChange = vm::onChangeContrasenna,
                label = { Text("Contraseña") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                // Ocultar o mostrar contraseña según estado
                visualTransformation = if (passwordVisible.value)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

                // Botón del “ojo” para alternar visibilidad
                trailingIcon = {
                    val icon = if (passwordVisible.value)
                        Icons.Filled.VisibilityOff
                    else
                        Icons.Filled.Visibility

                    val description =
                        if (passwordVisible.value) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onSaved, modifier = Modifier.weight(1f)) { Text("Registrarte") }
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Cancelar") }
            }
        }
    }
}
