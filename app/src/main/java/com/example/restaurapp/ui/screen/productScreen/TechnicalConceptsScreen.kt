package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.RestaurAppTheme

// --- ESTRUCTURA DE DATOS ---
data class SubItem(
    val title: String,
    val description: String
)

data class ConceptFamily(
    val title: String,
    val description: String,
    val imageRes: Int,
    val subItems: List<SubItem>
)

// --- LISTA COMPLETA DE DATOS PARA LA PANTALLA ---
val conceptList = listOf(
    ConceptFamily(
        title = "ARCO",
        description = "Construcción curva que cubre un vano...",
        imageRes = R.drawable.arco_image, // Asegúrate de tener esta imagen en drawable
        subItems = listOf(
            SubItem("Clave", "Dovela central en la cima del arco, pieza maestra para su sostenibilidad."),
            SubItem("Contraclaves", "Dovelas adyacentes a la clave."),
            SubItem("Riñones", "Dovelas que ocupan la zona intermedia entre el arranque y la clave."),
            SubItem("Salmer", "Dovela de arranque a cada lado del arco."),
            SubItem("Estribo, almohadón o imposta", "Sillar donde apea cada extremo del arco.")
        )
    ),
    ConceptFamily(
        title = "COLUMNA",
        description = "Elemento vertical de soporte...",
        imageRes = R.drawable.columna_image, // Asegúrate de tener esta imagen en drawable
        subItems = listOf(
            SubItem(
                "Capitel",
                "Coronamiento del fuste de una columna, pilar o pilastra tratado distintamente con varias molduras y que recibe el peso del entablamento."
            ),
            SubItem(
                "Fuste",
                "Parte central de una columna o pilar comprendida entre el capitel y la basa."
            ),
            SubItem(
                "Basa",
                "Parte inferior de una columna, pilar o pilastra, por lo general tratada distintamente con varias molduras. Se considera como una unidad arquitectónica."
            ),
            SubItem(
                "Dado",
                "Parte del pedestal comprendida entre la basa y la cornisa del mismo."
            )
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicalConceptsScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diccionario de Restauración") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item { SearchBar() }
            item { ConceptTabs() }

            // Iteramos sobre la lista de datos para crear las tarjetas
            items(conceptList) { family ->
                ConceptFamilyItem(family = family)
            }
        }
    }
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar concepto...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        shape = CircleShape
    )
}

@Composable
fun ConceptTabs() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Familias", "Todos los Conceptos")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { Text(title) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConceptFamilyItem(family: ConceptFamily) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        // Tarjeta principal que controla la expansión
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = family.imageRes),
                    contentDescription = family.title,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = family.title, fontWeight = FontWeight.Bold)
                    Text(text = family.description, fontSize = 14.sp, color = Color.Gray)
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expandir",
                    // Pequeña animación para girar la flecha
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }
        }

        // Contenido que aparece y desaparece con animación
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                family.subItems.forEach { subItem ->
                    SubItemCard(subItem = subItem)
                }
            }
        }
    }
}

// Composable para cada tarjeta de sub-ítem
@Composable
fun SubItemCard(subItem: SubItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = subItem.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subItem.description, fontSize = 14.sp)
        }
    }
}


@Preview(showBackground = true, name = "Pantalla Conceptos Técnicos")
@Composable
fun TechnicalConceptsScreenPreview() {
    RestaurAppTheme {
        TechnicalConceptsScreen(onNavigateBack = {})
    }
}
