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
fun ProductScreenCompact(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Buscar en el diccionario...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "BLOQUES DE CONOCIMIENTO",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta 1
            ConceptCard(
                text = "CONCEPTOS FORMATIVOS",
                iconRes = R.drawable.laurel,
                color = Color(0xFFC0A069),
                modifier = Modifier.weight(1f)
            )
            // Tarjeta 2
            ConceptCard(
                text = "CONCEPTOS TÉCNICOS",
                iconRes = R.drawable.capitel,
                color = Color(0xFF3B6B9C),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// He movido la tarjeta a su propio Composable para reutilizarla y mantener el código limpio
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConceptCard(
    text: String,
    iconRes: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { /* TODO */ },
        modifier = modifier.height(180.dp), // Altura fija para consistencia
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
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
                contentDescription = null, // Es decorativo
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
        ProductScreenCompact()
    }
}
