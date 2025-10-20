package com.example.restaurapp.ui.screens.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R
import com.example.restaurapp.ui.components.ConceptCard
import com.example.restaurapp.ui.theme.DuocBlue
import com.example.restaurapp.ui.theme.DuocYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBase(
    modifier: Modifier = Modifier,
    mainPadding: Dp,
    titleStyle: TextStyle,
    spaceAfterSearch: Dp,
    spaceAfterTitle: Dp,
    onNavigateToAddContent: () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(mainPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Usamos Arrangement para un espaciado consistente
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Buscar en el diccionario...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape
        )

        Text(
            text = "BLOQUES DE CONOCIMIENTO",
            style = titleStyle,
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 2. CONECTA LOS EVENTOS A LAS TARJETAS EXISTENTES
            // IMPORTANTE: Esto asume que tu Composable ConceptCard tiene un parámetro onClick
            ConceptCard(
                text = "CONCEPTOS FORMATIVOS",
                iconRes = R.drawable.laurel,
                color = DuocYellow,
                onClick = onNavigateToAddContent, // Notifica el evento de clic
                modifier = Modifier.weight(1f)
            )
            ConceptCard(
                text = "CONCEPTOS TÉCNICOS",
                iconRes = R.drawable.capitel,
                color = DuocBlue,
                onClick = onNavigateToAddContent, // Notifica el evento de clic
                modifier = Modifier.weight(1f)
            )
        }

        // 3. AÑADE LA NUEVA TARJETA PARA "AÑADIR CONTENIDO"
        Card(
            onClick = onNavigateToAddContent, // Notifica el nuevo evento de clic
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir Contenido",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "AÑADIR NUEVO CONCEPTO",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
