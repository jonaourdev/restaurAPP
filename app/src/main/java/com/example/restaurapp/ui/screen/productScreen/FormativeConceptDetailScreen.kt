package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.RestaurAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormativeConceptDetailScreen(
    // Recibe el ID del concepto a mostrar y la acción para volver
    conceptId: String?,
    onNavigateBack: () -> Unit
) {
    // Buscamos el concepto en nuestra lista de datos usando el ID
    // Nota: El '?' y el 'firstOrNull' manejan el caso de que el ID sea nulo o no se encuentre.
    val concept = formativeConceptsList.firstOrNull { it.id == conceptId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // El título se toma del concepto encontrado
                    Text(
                        text = concept?.title ?: "Detalle",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                // Dejamos un espacio para que el título se centre correctamente
                actions = { IconButton(onClick = {}, enabled = false) {} }
            )
        }
    ) { paddingValues ->
        // Usamos Column con scroll vertical por si el texto es muy largo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (concept != null) {
                // Mostramos la imagen del concepto
                Image(
                    painter = painterResource(id = concept.imageRes),
                    contentDescription = concept.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                // Mostramos la descripción completa del concepto
                Text(
                    text = concept.fullDescription,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                // Mensaje de error si no se encuentra el concepto
                Text("Concepto no encontrado.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormativeConceptDetailScreenPreview() {
    RestaurAppTheme {
        // En la preview, mostramos el primer concepto de la lista como ejemplo
        FormativeConceptDetailScreen(conceptId = "patrimonio", onNavigateBack = {})
    }
}
