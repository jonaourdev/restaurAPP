package com.example.restaurapp.ui.screens.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMedium(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp), // Más padding para pantallas más grandes
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Columna Izquierda: Búsqueda y Título
        Column(modifier = Modifier.weight(0.8f)) {
            Text(
                text = "BLOQUES DE CONOCIMIENTO",
                style = MaterialTheme.typography.headlineMedium, // Texto más grande
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Buscar en el diccionario...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape
            )
        }

        // Columna Derecha: Tarjetas
        Row(
            modifier = Modifier.weight(1.2f),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Reutilizamos la misma tarjeta, pero ahora es más grande
            // Tarjeta de Conceptos Formativos
            Card(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC0A069)) // Color dorado
            ) {
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
            // Tarjeta de Conceptos Técnicos
            Card(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B6B9C)) // Color azul
            ) {
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
