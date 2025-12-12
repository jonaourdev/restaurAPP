// Contenido 100% corregido y actualizado para FavoritesScreenBase.kt

package com.example.restaurapp.ui.screens.favoritesScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.ui.screens.listConceptScreen.ConceptListItem
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

private data class ConceptoFavorito(
    val id: Long,
    val name: String,
    val description: String,
    val isFavorite: Boolean,
    val type: String
)

@Composable
fun FavoritesScreenBase(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    authVm: AuthViewModel,
    onNavigateToConceptDetail: (conceptId: Long) -> Unit,
    gridCells: GridCells
) {
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()

    // 1. Mapeamos todos los conceptos a nuestra nueva clase de datos unificada
    val todosLosConceptos = uiState.conceptosFormativos.map { formativo ->
        ConceptoFavorito(
            id = formativo.formativeCId,
            name = formativo.formativeName,
            description = formativo.formativeDescription,
            isFavorite = formativo.isFavorite,
            type = ConceptType.FORMATIVO
        )
    } + uiState.conceptosTecnicos.map { tecnico ->
        ConceptoFavorito(
            id = tecnico.technicalId,
            name = tecnico.technicalName,
            description = tecnico.technicalDescription,
            isFavorite = tecnico.isFavorite,
            type = ConceptType.TECNICO
        )
    }

    // 2. FILTRAMOS SOLO LOS QUE SON FAVORITOS
    val favoriteConcepts = todosLosConceptos.filter { it.isFavorite }

    Box(
        modifier = modifier.fillMaxSize(),
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
            favoriteConcepts.isEmpty() -> {
                Text(
                    text = "Aún no has añadido ningún concepto a favoritos.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = gridCells,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = favoriteConcepts,
                        key = { "${it.type}-${it.id}" }
                    ) { concepto -> // 'concepto' es ahora de tipo 'ConceptoFavorito'
                        // Ahora Kotlin entiende perfectamente qué es 'concepto' y sus propiedades
                        ConceptListItem(
                            conceptName = concepto.name,
                            conceptDescription = concepto.description,
                            isFavorite = concepto.isFavorite,
                            onClick = { onNavigateToConceptDetail(concepto.id) },
                            onFavoriteClick = {
                                authState.currentUser?.id?.let { userId ->
                                    vm.toggleFavorite(userId, concepto.id, concepto.isFavorite)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
