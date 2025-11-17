package com.example.restaurapp.ui.screens.listConceptScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder // CAMBIO: Usamos Outlined
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.restaurapp.model.local.concepts.ConceptType
// IMPORTAMOS LOS NUEVOS DTOs DE RED
import com.example.restaurapp.model.network.ConceptoFormativoNetworkDTO
import com.example.restaurapp.model.network.FamiliaNetworkDTO
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListConceptScreenBase(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: () -> Unit,
    onNavigateToFamily: (Long) -> Unit,
    onNavigateToConceptDetail: (conceptId: Long) -> Unit,
    tipoConcepto: String,
    authVm: AuthViewModel,
    contentPadding: PaddingValues,
    gridCells: GridCells,
    itemSpacing: Dp
) {
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()
    val currentUserId = authState.currentUser?.id ?: 0 // 0 para invitado
    val isGuest = authState.currentUser == null
    val context = LocalContext.current

    Scaffold(
        // ... (Tu TopBar y FAB se mantienen igual)
        modifier = modifier,
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
                uiState.isLoading -> CircularProgressIndicator()
                uiState.error != null -> Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)

                else -> {
                    LazyVerticalGrid(
                        columns = gridCells,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(itemSpacing),
                        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
                    ) {
                        if (tipoConcepto == ConceptType.TECNICO) {
                            // --- Lógica para Familias Técnicas ---
                            if (uiState.families.isEmpty()) {
                                item { Text("No hay familias. Presiona '+' para crear una.", textAlign = TextAlign.Center) }
                            } else {
                                items(
                                    items = uiState.families,
                                    key = { family -> family.familyId }
                                ) { family ->
                                    FamilyListItem(
                                        family = family,
                                        onClick = { onNavigateToFamily(family.familyId) }
                                    )
                                }
                            }
                        } else {
                            // --- Lógica para Conceptos Formativos (ACTUALIZADA) ---
                            val conceptosFiltrados = uiState.conceptosFormativos

                            if (conceptosFiltrados.isEmpty()) {
                                item { Text("No hay conceptos formativos. Presiona '+' para añadir uno.", textAlign = TextAlign.Center) }
                            } else {
                                items(
                                    items = conceptosFiltrados,
                                    key = { it.formativeCId }
                                ) { concepto ->
                                    ConceptFormativoListItem( // Usamos un Composable específico
                                        concepto = concepto,
                                        onFavoriteClick = {
                                            if (isGuest) {
                                                Toast.makeText(context, "Inicia sesión para guardar favoritos", Toast.LENGTH_SHORT).show()
                                            } else {
                                                vm.toggleFavorite(currentUserId, concepto.formativeCId, concepto.isFavorite)
                                            }
                                        },
                                        onClick = {
                                            onNavigateToConceptDetail(concepto.formativeCId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Composable para un item de Concepto Formativo
 */
@Composable
fun ConceptFormativoListItem(
    concepto: ConceptoFormativoNetworkDTO,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = concepto.formativeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = concepto.formativeDescription.take(100) + "...", // Acortamos descripción
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (concepto.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (concepto.isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}

/**
 * Composable para un item de Familia (sin cambios)
 */
@Composable
fun FamilyListItem(family: FamiliaNetworkDTO, onClick: () -> Unit) {
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
                Text(family.familyName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (family.familyDescription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(family.familyDescription, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Ver detalles")
        }
    }
}