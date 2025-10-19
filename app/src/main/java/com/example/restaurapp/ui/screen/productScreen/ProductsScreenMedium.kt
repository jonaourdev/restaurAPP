package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.RestaurAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreenMedium(
    modifier: Modifier = Modifier,
    onNavigateToTechnical: () -> Unit,
    onNavigateToFormative: () -> Unit,
    onNavigateToAdd: () -> Unit // <-- Parámetro para la nueva navegación
) {
    // Usamos una cuadrícula para distribuir mejor las tarjetas en pantallas medianas
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Dos columnas
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Barra de Búsqueda (ocupa las dos columnas) ---
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Buscar en el diccionario...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.height(16.dp))
        }

        // --- Título (ocupa las dos columnas) ---
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "BLOQUES DE CONOCIMIENTO",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // --- Tarjetas de Conceptos ---
        item {
            ConceptCard(
                text = "CONCEPTOS FORMATIVOS",
                iconRes = R.drawable.laurel,
                color = Color(0xFFC0A069),
                modifier = Modifier.height(200.dp),
                onClick = onNavigateToFormative
            )
        }
        item {
            ConceptCard(
                text = "CONCEPTOS TÉCNICOS",
                iconRes = R.drawable.capitel,
                color = Color(0xFF3B6B9C),
                modifier = Modifier.height(200.dp),
                onClick = onNavigateToTechnical
            )
        }

        // --- Tarjeta para Agregar Contenido (ocupa las dos columnas) ---
        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToAdd, // Llama a la acción de navegación
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Agregar Contenido")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Agregar Contenido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 480)
@Composable
fun ProductScreenMediumPreview() {
    RestaurAppTheme {
        ProductScreenMedium(
            onNavigateToTechnical = {},
            onNavigateToFormative = {},
            onNavigateToAdd = {}
        )
    }
}
