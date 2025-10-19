package com.example.restaurapp.ui.screens.productScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
fun ProductScreenExpanded(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(64.dp), // Aún más padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Columna Izquierda: Título y Búsqueda
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "BLOQUES DE CONOCIMIENTO",
                style = MaterialTheme.typography.headlineLarge, // El texto más grande
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
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(), // Correcto: ocupa el ancho de la columna padre
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC0A069))
            ) {
                // Esta columna interna está bien, no necesita cambios
                Column(
                    modifier = Modifier
                        .padding(vertical = 24.dp, horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("CONCEPTOS FORMATIVOS", color = Color.White, textAlign = TextAlign.Center)
                    Icon(painterResource(id = R.drawable.laurel), contentDescription = null, tint = Color.White)
                }
            }

            // Tarjeta de Conceptos Técnicos (CORREGIDA)
            Card(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(), // Correcto: ocupa el ancho de la columna padre
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B6B9C))
            ) {
                // Esta columna interna también está bien, no necesita cambios
                Column(
                    modifier = Modifier
                        .padding(vertical = 24.dp, horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("CONCEPTOS TÉCNICOS", color = Color.White, textAlign = TextAlign.Center)
                    Icon(painterResource(id = R.drawable.capitel), contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 800) // Cambié showBackground a true
@Composable
fun ProductScreenExpandedPreview(){
    RestaurAppTheme {
        ProductScreenExpanded()
    }
}
