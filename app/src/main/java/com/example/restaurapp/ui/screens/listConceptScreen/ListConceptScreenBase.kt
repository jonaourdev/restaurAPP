package com.example.restaurapp.ui.screens.listConceptScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
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
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.local.concepts.FamilyEntity
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel
import androidx.compose.foundation.lazy.grid.items

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
    val isGuest = authState.currentUser == null
    val context = LocalContext.current

    Scaffold(
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
                                item {
                                    Text(
                                        text = "Aún no hay familias de conceptos técnicos. ¡Presiona '+' para crear la primera!",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                items(
                                    items = uiState.families,
                                    key = { family -> family.id }
                                ) { family ->
                                    FamilyListItem(
                                        family = family,
                                        onClick = { onNavigateToFamily(family.id) }
                                    )
                                 }
                            }
                        } else {
                            val conceptosFiltrados = uiState.concepts.filter { it.tipo == tipoConcepto }
                            val (favoriteConcepts, otherConcepts) = conceptosFiltrados.partition { it.isFavorite }

                            if (conceptosFiltrados.isEmpty()) {
                                item {
                                    Text(
                                        text = "Aún no hay conceptos formativos. ¡Presiona '+' para añadir el primero!",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                if (favoriteConcepts.isNotEmpty()) {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Text(
                                            text = "Favoritos",
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                                        )
                                    }
                                    items(favoriteConcepts) { concept ->
                                        ConceptListItem(
                                            concept = concept,
                                            onFavoriteClick = { vm.toggleFavorite(concept) },
                                            onClick = {onNavigateToConceptDetail(concept.id)}
                                        )
                                    }
                                }

                                if (otherConcepts.isNotEmpty()) {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Text(
                                            text = if (favoriteConcepts.isNotEmpty()) "Otros Conceptos" else "Conceptos",
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                                        )
                                    }
                                    items(otherConcepts) { concept ->
                                        ConceptListItem(
                                            concept = concept,
                                            onFavoriteClick = { vm.toggleFavorite(concept) },
                                            onClick = {onNavigateToConceptDetail(concept.id)}
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
}


@Composable
fun ConceptListItem(concept: ConceptEntity, onFavoriteClick: () -> Unit, onClick: () -> Unit) {
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
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (concept.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Favorite",
                    tint = if (concept.isFavorite) Color.Yellow else Color.Gray
                )
            }
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