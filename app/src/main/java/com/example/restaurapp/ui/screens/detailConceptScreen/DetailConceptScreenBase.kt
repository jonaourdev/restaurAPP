package com.example.restaurapp.ui.screens.detailConceptScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.restaurapp.R
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailConceptScreenBase(
    modifier: Modifier = Modifier,
    conceptId: Long,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    contentPadding: Dp,
    imageHeight: Dp,
    contentWidthFraction: Float
) {
    LaunchedEffect(conceptId) {
        vm.selectConceptById(conceptId)
    }

    val uiState by vm.uiState.collectAsState()
    val selectedConcept = uiState.selectedConcept

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedConcept?.nombreConcepto ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (selectedConcept == null || selectedConcept.id.toLong() != conceptId) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                    AsyncImage(
                        model = selectedConcept.imageUrl,
                        contentDescription = "Imagen de ${selectedConcept.nombreConcepto}",
                        placeholder = painterResource(id = R.drawable.ic_default_avatar),
                        error = painterResource(id = R.drawable.ic_default_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Text(
                        text = selectedConcept.nombreConcepto,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = selectedConcept.descripcion,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}