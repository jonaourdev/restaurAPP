package com.example.restaurapp.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {

    val form by vm.form.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RestaurApp") })
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

            OutlinedTextField(
                value = form.contrasenna,
                onValueChange = vm::onChangeContrasenna,
                label = { Text("Contraseña") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onSaved, modifier = Modifier.weight(1f)) { Text("Registrarte") }
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Cancelar") }
            }
        }
    }
}
