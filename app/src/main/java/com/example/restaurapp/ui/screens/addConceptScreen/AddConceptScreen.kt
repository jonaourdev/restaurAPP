package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.RestaurAppTheme

// Enum se queda aquí o en un archivo común
enum class ContentType {
    Familia,
    ConceptoFormativo,
    ConceptoTecnico
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(
    windowSizeClass: WindowWidthSizeClass,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agregar Contenido",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) }
            )
        }
    ) { paddingValues ->
        when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> AddContentScreenCompact(modifier = Modifier.padding(paddingValues))
            WindowWidthSizeClass.Medium -> AddContentScreenMedium(modifier = Modifier.padding(paddingValues))
            WindowWidthSizeClass.Expanded -> AddContentScreenExpanded(modifier = Modifier.padding(paddingValues))
        }
    }
}

// --- Componentes de Formulario Reutilizables ---
// Mueve aquí TODOS los composables de los formularios para evitar duplicación de código.
// (ContentTypeSelector, FamilyForm, FormativeConceptForm, TechnicalConceptForm, ImageUploadButton)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentTypeSelector(
    selectedType: ContentType,
    onTypeSelected: (ContentType) -> Unit
) {
    val tabOptions = listOf(ContentType.Familia, ContentType.ConceptoFormativo, ContentType.ConceptoTecnico)
    val selectedTabIndex = tabOptions.indexOf(selectedType)

    TabRow(selectedTabIndex = selectedTabIndex) {
        tabOptions.forEachIndexed { index, type ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTypeSelected(type) },
                text = {
                    // El texto que se mostrará en cada pestaña
                    val label = when (type) {
                        ContentType.Familia -> "Familia"
                        ContentType.ConceptoFormativo -> "Concepto Formativo"
                        ContentType.ConceptoTecnico -> "Concepto Técnico"
                    }
                    Text(label)
                }
            )
        }
    }
}

@Composable
fun FamilyForm() {
    // ... (El código de FamilyForm no cambia)
    var familyName by remember { mutableStateOf("") }
    var familyDescription by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = familyName,
            onValueChange = { familyName = it },
            label = { Text("Nombre de la Familia") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = familyDescription,
            onValueChange = { familyDescription = it },
            label = { Text("Descripción de la Familia") },
            modifier = Modifier.fillMaxWidth()
        )
        ImageUploadButton()
    }
}

@Composable
fun FormativeConceptForm() {
    // ... (El código de FormativeConceptForm no cambia)
    var conceptName by remember { mutableStateOf("") }
    var conceptDescription by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = conceptName,
            onValueChange = { conceptName = it },
            label = { Text("Nombre del Concepto") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = conceptDescription,
            onValueChange = { conceptDescription = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        ImageUploadButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicalConceptForm() {
    // ... (El código de TechnicalConceptForm no cambia)
    var conceptName by remember { mutableStateOf("") }
    var conceptDescription by remember { mutableStateOf("") }
    val families = listOf("Arco", "Columna")
    var expanded by remember { mutableStateOf(false) }
    var selectedFamily by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = conceptName,
            onValueChange = { conceptName = it },
            label = { Text("Nombre del Concepto") },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                value = selectedFamily,
                onValueChange = {},
                label = { Text("Familia Perteneciente") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                families.forEach { family ->
                    DropdownMenuItem(
                        text = { Text(family) },
                        onClick = {
                            selectedFamily = family
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = conceptDescription,
            onValueChange = { conceptDescription = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        ImageUploadButton()
    }
}

@Composable
fun ImageUploadButton() {
    // ... (El código de ImageUploadButton no cambia)
    OutlinedButton(
        onClick = { /* Lógica de carga */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.load_image),
            contentDescription = "Cargar Imagen",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Cargar Imagen (Opcional)")
    }
}

// --- Vistas Previas para AddContentScreen ---
@Preview(name = "AddContent - Compact", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun AddContentScreenCompactMainPreview() {
    RestaurAppTheme {
        AddContentScreen(windowSizeClass = WindowWidthSizeClass.Compact, onNavigateBack = {})
    }
}