package com.example.restaurapp.ui.screens.familyDetailScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.restaurapp.ui.screens.listConceptScreen.ConceptListItem // Reutilizamos este Composable
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
    onNavigateToSubfamilyDetail: (subfamilia: Long) -> Unit,
){
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()
    val isGuest = authState.currentUser == null
    val context = LocalContext.current

    // CORRECCIÓN 1: Buscar la familia en la lista de familias del UiState.
    val family = uiState.families.find { it.id == familyId }

    // CORRECCIÓN 2: Los conceptos ya están dentro del objeto 'family'. No hay que filtrar nada más.
    val subfamiliasInFamily = uiState.subfamilies.filter { it.familiaId == familyId }

    LaunchedEffect(familyId) {
        vm.loadSubfamilies(familyId)
    }

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
                        Toast.makeText(context, "Los invitados no pueden añadir.", Toast.LENGTH_SHORT).show()
                    } else {
                        onNavigateToAddConcept(familyId)
                    }
                }
            ) {
                Icon(Icons.Default.Add, "Añadir Concepto a esta familia")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.error != null -> Text(
                    "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )

                family == null -> Text("Familia no encontrada.") // Mensaje si la familia no se carga
                subfamiliasInFamily.isEmpty() -> Text(
                    "No hay subfamilias en esta familia.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                else -> {
                    LazyVerticalGrid(
                        columns = gridCells,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(itemSpacing),
                        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
                    ) {
                        items(
                            items = subfamiliasInFamily,
                            key = { "SUBFAMILIA-${it.id}" }
                        ) { subfamilia ->
                            ConceptListItem(
                                conceptName = subfamilia.name,
                                conceptDescription = subfamilia.description,
                                isFavorite = false,
                                onClick = {
                                    onNavigateToSubfamilyDetail(subfamilia.id) /*CAMBIAR A DETALLE SUBFAMILIA*/
                                },
                                onFavoriteClick = {/*NADA POR AHORA*/ }
                            )
                        }

                    }
                }
            }
        }
    }
}
