package com.example.restaurapp.ui.screens.addFamilyScreeen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.ConceptViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFamilyScreenBase(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    formWidth: Float,
    authVm: AuthViewModel
){
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()

    // Manejo de éxito o error
    LaunchedEffect(uiState.successMessage, uiState.error) {
        // Si hay un mensaje de éxito, volvemos atrás
        if (uiState.successMessage != null) {
            vm.clearMessages()
            onNavigateBack()
        }
        // Si hay error, no navegamos, dejamos que se muestre en el texto de error
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Añadir Familia") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ){ paddingValues ->
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
                FamilyForm(
                    uiState = uiState,
                    onNameChange = vm::onFamilyNameChange,
                    onDescriptionChange = vm::onFamilyDescriptionChange,
                    onSaveClick = {
                        authState.currentUser?.id?.let { userId ->
                            // El ViewModel ahora debe llamar al repositorio sin el campo componentes
                            vm.addFamily(userId)
                        }
                    }
                )

                if (uiState.isLoading) {
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator()
                    Text("Guardando familia...")
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
fun FamilyForm(
    uiState: ConceptUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Crear una nueva familia",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Campo para el nombre de la familia
        OutlinedTextField(
            value = uiState.familyName,
            onValueChange = onNameChange,
            label = { Text("Nombre de la Familia") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.error != null,
            singleLine = true,
            enabled = !uiState.isLoading
        )

        // Campo para la descripción de la familia
        OutlinedTextField(
            value = uiState.familyDescription,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción de la Familia") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            isError = uiState.error != null,
            enabled = !uiState.isLoading
        )

        // NOTA: Se eliminó el campo "Componentes" que causaba conflicto con el backend

        // Botón para guardar
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            // Validación corregida: solo requiere nombre y descripción
            enabled = !uiState.isLoading && uiState.familyName.isNotBlank() && uiState.familyDescription.isNotBlank()
        ) {
            Text(
                text = if (uiState.isLoading) "Guardando..." else "Guardar familia",
                fontSize = 16.sp
            )
        }
    }
}