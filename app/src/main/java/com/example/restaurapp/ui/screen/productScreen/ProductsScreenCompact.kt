package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun ProductScreenCompact(
    modifier: Modifier = Modifier,
    onNavigateToTechnical: () -> Unit,
    onNavigateToFormative: () -> Unit,
    onNavigateToAdd: () -> Unit
) {
    // Usamos LazyColumn para asegurar que sea scrollable si el contenido crece
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre los items
    ) {
        // --- Barra de Búsqueda ---
        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Buscar en el diccionario...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape
            )
        }

        item { Spacer(Modifier.height(16.dp)) }

        // --- Título ---
        item {
            Text(
                text = "BLOQUES DE CONOCIMIENTO",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // --- Fila con las dos tarjetas principales ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta 1: Conceptos Formativos
                ConceptCard(
                    text = "CONCEPTOS FORMATIVOS",
                    iconRes = R.drawable.laurel,
                    color = Color(0xFFC0A069),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToFormative
                )
                // Tarjeta 2: Conceptos Técnicos
                ConceptCard(
                    text = "CONCEPTOS TÉCNICOS",
                    iconRes = R.drawable.capitel,
                    color = Color(0xFF3B6B9C),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToTechnical
                )
            }
        }

        // --- Tarjeta para Agregar Contenido ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToAdd, // Llama a la acción de navegación
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Agregar nuevo concepto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id= R.drawable.new_concept),
                        contentDescription = "Agregar Concepto",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConceptCard(
    text: String,
    iconRes: Int,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(180.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenCompactPreview() {
    RestaurAppTheme {
        ProductScreenCompact(
            onNavigateToTechnical = {},
            onNavigateToFormative = {},
            onNavigateToAdd = {} // <-- Actualizado para la preview
        )
    }
}
