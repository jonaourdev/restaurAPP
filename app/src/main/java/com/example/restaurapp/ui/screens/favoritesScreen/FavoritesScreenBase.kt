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
import com.example.restaurapp.model.local.concepts.ConceptWithFavorite // Importa el nuevo modelo
import com.example.restaurapp.ui.screens.listConceptScreen.ConceptListItem
import com.example.restaurapp.viewmodel.AuthViewModel // Importa el AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun FavoritesScreenBase(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    authVm: AuthViewModel, // <-- AÑADIDO: Necesitamos el AuthViewModel para el userId
    onNavigateToConceptDetail: (conceptId: Long) -> Unit,
    gridCells: GridCells
) {
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState() // Obtenemos el estado de autenticación

    // La lista 'uiState.concepts' ahora es de tipo List<ConceptWithFavorite>
    // Filtramos para obtener SÓLO los que tienen isFavorite = true
    val favoriteConcepts = uiState.concepts.filter { it.isFavorite }

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
            // Mostramos un mensaje si la lista de favoritos está vacía
            favoriteConcepts.isEmpty() -> {
                Text(
                    text = "Aún no has añadido ningún concepto a favoritos.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            // Mostramos la lista de favoritos si no está vacía
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
                        key = { it.concept.id } // La key es el id del concepto anidado
                    ) { conceptWithFavorite -> // El item ahora es de tipo ConceptWithFavorite
                        // Reutilizamos el mismo ConceptListItem
                        ConceptListItem(
                            conceptWithFavorite = conceptWithFavorite, // Pasamos el objeto completo
                            onFavoriteClick = {
                                // Solo permite la acción si hay un usuario logueado
                                authState.currentUser?.id?.let { userId ->
                                    vm.toggleFavorite(conceptWithFavorite, userId)
                                }
                            },
                            onClick = {
                                // La navegación no cambia, solo necesita el ID del concepto
                                onNavigateToConceptDetail(conceptWithFavorite.concept.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
