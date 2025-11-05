package com.example.restaurapp.ui.screens.listConceptScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navArgument
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.local.concepts.FamilyEntity
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.AuthViewModelFactory
import com.example.restaurapp.viewmodel.ConceptViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListConceptScreen(
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: () -> Unit,
    onNavigateToFamily: (Long) -> Unit,
    tipoConcepto: String,
    authVm: AuthViewModel
) {
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()
    val isGuest = authState.currentUser == null
    val context = LocalContext.current

    Scaffold(
        topBar = {
            val titleText = if (tipoConcepto == ConceptType.TECNICO) "Familias de Conceptos" else "Conceptos Formativos"
            TopAppBar(
                title = { Text(titleText) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (isGuest) {
                    Toast.makeText(
                        context,
                        "Los invitados no pueden añadir. Por favor, regístrese.", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onNavigateToAddConcept()
                }
            }) {
                val iconDesc = if (tipoConcepto == ConceptType.TECNICO) "Añadir Familia" else "Añadir Concepto"
                Icon(Icons.Default.Add, contentDescription = iconDesc)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    if (tipoConcepto == ConceptType.TECNICO) {
                        // --- Lógica para Familias Técnicas ---
                        if (uiState.families.isEmpty()) {
                            Text(
                                text = "Aún no hay familias de conceptos técnicos.\n¡Presiona '+' para crear la primera!",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.families) { family ->
                                    FamilyListItem(family = family, onClick = {
                                        onNavigateToFamily(family.id)
                                    })
                                }
                            }
                        }
                    } else {
                        val conceptosFiltrados = uiState.concepts.filter { it.tipo == tipoConcepto }
                        if (conceptosFiltrados.isEmpty()) {
                            Text(
                                text = "Aún no hay conceptos formativos.\n¡Presiona '+' para añadir el primero!",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(conceptosFiltrados) { concept ->
                                    ConceptListItem(concept = concept)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ConceptListItem(concept: ConceptEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = concept.nombreConcepto,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = concept.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FamilyListItem(family: FamilyEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(family.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (!family.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(family.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Ver detalles")
        }
    }
}



