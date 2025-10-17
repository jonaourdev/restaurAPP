package com.example.restaurapp.ui.screens.registerScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterContentPreviewable(
    nombre: String = "",
    correo: String = "",
    contrasenna: String = "",
    error: String? = null,
    onNombre: (String) -> Unit = {},
    onCorreo: (String) -> Unit = {},
    onContrasenna: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onSaved: () -> Unit = {}
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Registro") }) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(nombre, onNombre, label = { Text("Nombre completo") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(correo, onCorreo, label = { Text("Correo") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(contrasenna, onContrasenna, label = { Text("Contraseña") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onSaved, modifier = Modifier.weight(1f)) { Text("Registrarte") }
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Cancelar") }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterContentPreviewable(
        nombre = "",
        correo = "",
        contrasenna = ""
    )
}
