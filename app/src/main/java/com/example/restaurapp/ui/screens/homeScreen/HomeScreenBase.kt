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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.DuocBlue
import com.example.restaurapp.ui.theme.DuocYellow
import com.example.restaurapp.ui.components.ConceptCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBase(
    modifier: Modifier = Modifier,
    mainPadding: Dp,
    titleStyle: TextStyle,
    spaceAfterSearch: Dp,
    spaceAfterTitle: Dp
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(mainPadding),
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

        Spacer(Modifier.height(spaceAfterSearch))

        Text(
            text = "BLOQUES DE CONOCIMIENTO",
            style = titleStyle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(spaceAfterTitle))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta 1
            ConceptCard(
                text = "CONCEPTOS FORMATIVOS",
                iconRes = R.drawable.laurel,
                color = DuocYellow,
                modifier = Modifier.weight(1f)
            )
            // Tarjeta 2
            ConceptCard(
                text = "CONCEPTOS TÉCNICOS",
                iconRes = R.drawable.capitel,
                color = DuocBlue,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

