package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurapp.R
import com.example.restaurapp.viewmodel.ConceptUiState
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddConceptScreen(
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit
) {
    // 2. Observa el estado directamente del ViewModel
    val uiState by vm.uiState.collectAsState()

    // Lógica para mostrar mensajes (ej. con un Snackbar)
    LaunchedEffect(uiState.successMessage, uiState.error) {
        if (uiState.successMessage != null || uiState.error != null) {
            // Aquí mostrarías un Snackbar o Toast.
            // Es importante llamar a clearMessages() después para que no se muestre de nuevo.
            vm.clearMessages()
        }
    }

    Scaffold(
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 3. Usa un único formulario, pasándole el estado y los eventos del ViewModel
            ConceptForm(
                uiState = uiState,
                onNameChange = vm::onConceptNameChange,
                onDescriptionChange = vm::onDescriptionChange,
                onSaveClick = vm::addConcept
            )

            // Muestra mensajes de estado
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

            uiState.successMessage?.let { successMsg ->
                Spacer(Modifier.height(16.dp))
                Text(
                    text = successMsg,
                    color = MaterialTheme.colorScheme.primary, // Un color verde o primario para el éxito
                    textAlign = TextAlign.Center
                )
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

        // Campo para el nombre del concepto
        OutlinedTextField(
            value = uiState.nombreConcepto, // Lee el valor del estado
            onValueChange = onNameChange,     // Notifica al ViewModel del cambio
            label = { Text("Nombre del Concepto") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.error != null, // Se puede marcar en rojo si hay un error
            singleLine = true,
            enabled = !uiState.isLoading // Deshabilita el campo mientras se está guardando
        )

        // Campo para la descripción
        OutlinedTextField(
            value = uiState.descripcion,      // Lee el valor del estado
            onValueChange = onDescriptionChange, // Notifica al ViewModel del cambio
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp), // Más altura para la descripción
            isError = uiState.error != null,
            enabled = !uiState.isLoading
        )

        // Botón de guardado
        Button(
            onClick = onSaveClick, // Llama a la función `addConcept` del ViewModel
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !uiState.isLoading // El botón se deshabilita durante la carga
        ) {
            Text(
                text = if (uiState.isLoading) "Guardando..." else "Guardar Concepto",
                fontSize = 16.sp
            )
        }
    }
}


