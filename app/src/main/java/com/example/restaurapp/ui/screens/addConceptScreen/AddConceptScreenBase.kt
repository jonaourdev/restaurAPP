package com.example.restaurapp.ui.screens.addConceptScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptUiState
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddConceptScreenBase(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    formWidth: Float,
    authVm: AuthViewModel
) {
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage, uiState.error) {
        if (uiState.successMessage != null || uiState.error != null) {
            onNavigateBack()
            vm.clearMessages()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { val tipo = uiState.tipoSeleccionado.lowercase()
                    .replaceFirstChar { it.uppercase() }
                    Text("Añadir Concepto $tipo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(formWidth)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ConceptForm(
                    uiState = uiState,
                    onNameChange = vm::onConceptNameChange,
                    onDescriptionChange = vm::onDescriptionChange,
                    onSaveClick = {
                        authState.currentUser?.id?.let { userId ->
                            vm.addConcept(userId.toLong())
                        }
                    }
                )
                if (uiState.isLoading) {
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator()
                    Text("Guardando concepto...")
                }

                uiState.error?.let { errorMsg ->
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun ConceptForm(
    uiState: ConceptUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Crear un nuevo concepto",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = uiState.nombreConcepto,
            onValueChange = onNameChange,
            label = { Text("Nombre del Concepto") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.error != null,
            singleLine = true,
            enabled = !uiState.isLoading
        )

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            isError = uiState.error != null,
            enabled = !uiState.isLoading
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !uiState.isLoading
        ) {
            Text(
                text = if (uiState.isLoading) "Guardando..." else "Guardar Concepto",
                fontSize = 16.sp
            )
        }
    }
}




