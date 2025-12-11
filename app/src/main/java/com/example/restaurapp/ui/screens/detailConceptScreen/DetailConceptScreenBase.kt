// Contenido corregido para DetailConceptScreenBase.kt

package com.example.restaurapp.ui.screens.detailConceptScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.restaurapp.R
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailConceptScreenBase(
    modifier: Modifier = Modifier,
    conceptId: Long,
    vm: ConceptViewModel,
    authVm: AuthViewModel,
    onNavigateBack: () -> Unit,
    contentPadding: Dp,
    imageHeight: Dp,
    contentWidthFraction: Float
) {
    LaunchedEffect(conceptId) {
        vm.selectConceptById(conceptId)
    }

    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()

    // CORRECCIÓN 1: Usar la nueva propiedad 'selectedConcept'
    val selectedConcept = uiState.selectedConcept

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedConcept?.technicalName ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                },
                actions = {
                    if (selectedConcept != null) {
                        IconButton(onClick = {
                            authState.currentUser?.id?.let { userId ->
                                vm.toggleFavorite(userId, selectedConcept.technicalId, selectedConcept.isFavorite)
                            }
                        }) {
                            Icon(
                                imageVector = if (selectedConcept.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Marcar como favorito",
                                tint = if (selectedConcept.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // CORRECCIÓN 2: Condición de carga actualizada
        if (selectedConcept == null || selectedConcept.technicalId != conceptId) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(contentWidthFraction)
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // CORRECCIÓN 3: Usar los campos del DTO
                    AsyncImage(
                        model = selectedConcept.imageUrl,
                        contentDescription = "Imagen de ${selectedConcept.technicalName}",
                        placeholder = painterResource(id = R.drawable.ic_default_avatar),
                        error = painterResource(id = R.drawable.ic_default_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(imageHeight).clip(RoundedCornerShape(12.dp))
                    )
                    Text(selectedConcept.technicalName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text(selectedConcept.technicalDescription, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
