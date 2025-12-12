package com.example.restaurapp.ui.screens.subfamilyDetailScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.restaurapp.ui.screens.listConceptScreen.ConceptListItem
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubfamilyDetailScreenBase(
    authVm: AuthViewModel,
    vm: ConceptViewModel,
    subfamiliaId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToConceptDetail: (Long) -> Unit,
    contentPadding: PaddingValues,
    gridCells: GridCells,
    itemSpacing: Dp
) {
    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()
    val isGuest = authState.currentUser == null
    val context = LocalContext.current

    val subfamilia = uiState.subfamilies.find { it.id == subfamiliaId }

    LaunchedEffect(subfamiliaId) {
        vm.loadConceptosTecnicosBySubfamilia(subfamiliaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subfamilia?.name ?: "Subfamilia") },
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
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()

                uiState.error != null -> Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )

                uiState.conceptosTecnicos.isEmpty() -> Text(
                    text = "No hay conceptos técnicos en esta subfamilia.",
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
                            items = uiState.conceptosTecnicos,
                            key = { it.technicalId }
                        ) { concepto ->
                            ConceptListItem(
                                conceptName = concepto.technicalName,
                                conceptDescription = concepto.technicalDescription,
                                isFavorite = concepto.isFavorite,
                                onClick = { onNavigateToConceptDetail(concepto.technicalId) },
                                onFavoriteClick = {
                                    if (isGuest) {
                                        Toast.makeText(
                                            context,
                                            "Los invitados no pueden marcar favoritos.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        authState.currentUser?.id?.let { userId ->
                                            vm.toggleFavorite(
                                                userId,
                                                concepto.technicalId,
                                                concepto.isFavorite
                                            )
                                        }
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
