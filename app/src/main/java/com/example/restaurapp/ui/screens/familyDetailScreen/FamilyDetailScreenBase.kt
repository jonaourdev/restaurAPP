package com.example.restaurapp.ui.screens.familyDetailScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.restaurapp.ui.screens.listConceptScreen.ConceptListItem
import com.example.restaurapp.viewmodel.ConceptViewModel
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.example.restaurapp.viewmodel.AuthViewModel

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
    itemSpacing: Dp
){
    val uiState by vm.uiState.collectAsState()

    //Buscar familia actual
    val family = uiState.families.find { it.id == familyId }

    //Filtrar conceptos que pertenecen a la familia
    val conceptInFamily = uiState.concepts.filter { it.familyId == familyId }

    val authState by authVm.uiState.collectAsState() // <-- 2. Obtén el estado de autenticación
    val isGuest = authState.currentUser == null     // <-- 3. Define si es invitado
    val context = LocalContext.current


    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(family?.name ?: "Detalle de Familia") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    } },
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

                conceptInFamily.isEmpty() -> {
                    Text(
                        text = "Esta familia aún no tienen conceptos. Presiona '+' para añadir el primero!",
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
                        items(conceptInFamily) { concept ->
                            ConceptListItem(concept = concept, onFavoriteClick = { vm.toggleFavorite(concept) })
                        }
                    }
                }
            }
        }
    }
}