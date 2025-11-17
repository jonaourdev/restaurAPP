package com.example.restaurapp.ui.screens.detailConceptScreen

import androidx.compose.foundation.layout.*import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.restaurapp.viewmodel.AuthViewModel // <-- IMPORTANTE: Importa AuthViewModel
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
    // Este efecto se asegura de que el ViewModel seleccione el concepto correcto
    // cuando la pantalla se abre o el conceptId cambia.
    LaunchedEffect(conceptId) {
        vm.selectConceptById(conceptId)
    }

    val uiState by vm.uiState.collectAsState()
    val authState by authVm.uiState.collectAsState()

    // 1. OBTENEMOS EL OBJETO CORRECTO: ConceptWithFavorite?
    val selectedConceptWithFavorite = uiState.selectedConcept

    Scaffold(
        topBar = {
            TopAppBar(
                // 2. ACCESO CORREGIDO: Accedemos a través del objeto 'concept' anidado
                title = { Text(selectedConceptWithFavorite?.concept?.nombreConcepto ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                // 3. AÑADIDO: Botón para marcar/desmarcar como favorito
                actions = {
                    // Solo mostramos el botón si el concepto se ha cargado
                    if (selectedConceptWithFavorite != null) {
                        IconButton(onClick = {
                            // Llamamos a toggleFavorite solo si hay un usuario logueado
                            authState.currentUser?.id?.let { userId ->
                                vm.toggleFavorite(selectedConceptWithFavorite, userId)
                            }
                        }) {
                            Icon(
                                imageVector = if (selectedConceptWithFavorite.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Marcar como favorito",
                                tint = if (selectedConceptWithFavorite.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // 4. CONDICIÓN CORREGIDA: Comparamos el ID del concepto anidado
        if (selectedConceptWithFavorite == null || selectedConceptWithFavorite.concept.id != conceptId) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Extraemos las variables para que el código sea más limpio
            val concept = selectedConceptWithFavorite.concept

            Box(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
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
                    // 5. ACCESO CORREGIDO: Usamos las propiedades del objeto 'concept'
                    AsyncImage(
                        model = concept.imageUrl,
                        contentDescription = "Imagen de ${concept.nombreConcepto}",
                        placeholder = painterResource(id = R.drawable.ic_default_avatar),
                        error = painterResource(id = R.drawable.ic_default_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Text(
                        text = concept.nombreConcepto,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = concept.descripcion,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
