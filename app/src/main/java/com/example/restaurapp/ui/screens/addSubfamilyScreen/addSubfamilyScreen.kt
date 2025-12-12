package com.example.restaurapp.ui.screens.addSubfamilyScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubfamilyScreen(
    vm: ConceptViewModel,
    authVm: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()

    // Resetea estado al entrar si es necesario
    LaunchedEffect(Unit) { vm.clearMessages() }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) onNavigateBack()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Añadir Subfamilia") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = uiState.subfamilyName, // Necesitas agregar este campo al ViewModel
                onValueChange = { vm.onSubfamilyNameChange(it) },
                label = { Text("Nombre Subfamilia") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.subfamilyDescription, // Necesitas agregar este campo al ViewModel
                onValueChange = { vm.onSubfamilyDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    authState.currentUser?.id?.let { userId ->
                        vm.addSubfamily(userId.toLong())
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Guardar Subfamilia")
            }
            if (uiState.isLoading) CircularProgressIndicator()
            uiState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}