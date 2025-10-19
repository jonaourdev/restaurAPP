package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
// Se añade el parámetro para la navegación
fun ProductScreenExpanded(modifier: Modifier = Modifier,
                          onNavigateToTechnical: () -> Unit,
                          onNavigateToFormative: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Columna Izquierda: Título y Búsqueda
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "BLOQUES DE CONOCIMIENTO",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Buscar en el diccionario...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape
            )
        }

        Spacer(Modifier.width(64.dp))

        // Columna Derecha: Tarjetas apiladas verticalmente
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Tarjeta de Conceptos Formativos
            Card(
                onClick = onNavigateToFormative,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC0A069))
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 8.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("CONCEPTOS FORMATIVOS", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Icon(painterResource(id = R.drawable.laurel), contentDescription = null, tint = Color.White)
                }
            }

            // Tarjeta de Conceptos Técnicos - Con la acción de navegar
            Card(
                onClick = onNavigateToTechnical, // <-- Se pasa la acción de navegación aquí
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B6B9C))
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 8.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("CONCEPTOS TÉCNICOS", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Icon(painterResource(id = R.drawable.capitel), contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
fun ProductScreenExpandedPreview() {
    RestaurAppTheme {
        // La vista previa llama a la función con una acción vacía
        ProductScreenExpanded(onNavigateToFormative = {}, onNavigateToTechnical = {})
    }
}
