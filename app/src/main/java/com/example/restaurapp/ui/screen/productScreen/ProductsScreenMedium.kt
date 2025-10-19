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
fun ProductScreenMedium(modifier: Modifier = Modifier,
                        onNavigateToTechnical: () -> Unit,
                        onNavigateToFormative: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título y barra de búsqueda en una fila
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "BLOQUES DE CONOCIMIENTO",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(32.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Buscar...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                modifier = Modifier.weight(1f),
                shape = CircleShape
            )
        }

        Spacer(Modifier.height(32.dp))

        // Tarjetas apiladas verticalmente
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Tarjeta 1
            Card(
                onClick = onNavigateToFormative,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC0A069))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("CONCEPTOS FORMATIVOS", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(painterResource(id = R.drawable.laurel), contentDescription = null, tint = Color.White)
                }
            }
            // Tarjeta 2 - Con la acción de navegar
            Card(
                onClick = onNavigateToTechnical, // <-- Se pasa la acción de navegación aquí
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B6B9C))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("CONCEPTOS TÉCNICOS", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(painterResource(id = R.drawable.capitel), contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 480)
@Composable
fun ProductScreenMediumPreview() {
    RestaurAppTheme {
        // La vista previa llama a la función con una acción vacía
        ProductScreenMedium(onNavigateToTechnical = {}, onNavigateToFormative = {})
    }
}
