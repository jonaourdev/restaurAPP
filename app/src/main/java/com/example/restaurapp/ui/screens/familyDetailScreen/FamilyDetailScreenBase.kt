package com.example.restaurapp.ui.screens.familyDetailScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // Asegúrate de que este import esté
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.restaurapp.ui.screens.listConceptScreen.ConceptListItem
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyDetailScreenBase(
    authVm: AuthViewModel,
    modifier: Modifier = Modifier,
    familyId: Long,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: (Long) -> Unit,
    contentPadding: PaddingValues,
    gridCells: GridCells,
    itemSpacing: Dp,
    onNavigateToConceptDetail: (conceptId: Long) -> Unit,
){
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()

    // 1. OBTENEMOS EL ESTADO DE AUTENTICACIÓN
    val isGuest = authState.currentUser == null
    val context = LocalContext.current

    // Buscar familia actual (sin cambios)
    val family = uiState.families.find { it.id == familyId }

    // 2. FILTRADO CORREGIDO: Ahora filtramos la lista de ConceptWithFavorite
    // Accedemos al `familyId` a través del objeto `concept` anidado.
    val conceptsInFamily = uiState.concepts.filter { it.concept.familyId == familyId }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(family?.name ?: "Detalle de Familia") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isGuest) {
                        Toast.makeText(
                            context,
                            "Los invitados no pueden añadir. Por favor, regístrese.", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onNavigateToAddConcept(familyId)
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Concepto a esta familia")
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

                uiState.error != null -> Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )

                conceptsInFamily.isEmpty() -> {
                    Text(
                        text = "Esta familia aún no tiene conceptos. ¡Presiona '+' para añadir el primero!",
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
                        // 3. LLAMADA A `items` ACTUALIZADA
                        items(
                            items = conceptsInFamily,
                            key = { it.concept.id } // La key es el id del concepto anidado
                        ) { conceptWithFavorite -> // El ítem ahora es de tipo ConceptWithFavorite
                            ConceptListItem(
                                // Pasamos el objeto completo a ConceptListItem
                                conceptWithFavorite = conceptWithFavorite,
                                onClick = { onNavigateToConceptDetail(conceptWithFavorite.concept.id) },
                                onFavoriteClick = {
                                    // Solo permite la acción si hay un usuario logueado
                                    authState.currentUser?.id?.let { userId ->
                                        vm.toggleFavorite(conceptWithFavorite, userId)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
