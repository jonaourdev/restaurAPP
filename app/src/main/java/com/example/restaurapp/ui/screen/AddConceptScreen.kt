package com.example.restaurapp.ui.screen // ▼▼▼ ERROR CORREGIDO AQUÍ ▼▼▼

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.RestaurAppTheme

// Enum para manejar el tipo de contenido seleccionado
enum class ContentType {
    Familia,
    ConceptoFormativo,
    ConceptoTecnico
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(
    onNavigateBack: () -> Unit
) {
    var selectedContentType by remember { mutableStateOf(ContentType.ConceptoTecnico) }

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
                actions = {
                    // Espaciador para centrar el título correctamente
                    Spacer(modifier = Modifier.width(48.dp))
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Para que el formulario sea scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selector de tipo de contenido
            ContentTypeSelector(
                selectedType = selectedContentType,
                onTypeSelected = { selectedContentType = it }
            )

            // El formulario cambia según el tipo de contenido seleccionado
            when (selectedContentType) {
                ContentType.Familia -> FamilyForm()
                ContentType.ConceptoFormativo -> FormativeConceptForm()
                ContentType.ConceptoTecnico -> TechnicalConceptForm()
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            // Botón para guardar el contenido
            Button(
                onClick = { /* Lógica para guardar se implementará más adelante */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Guardar ${
                        when (selectedContentType) {
                            ContentType.Familia -> "Familia"
                            ContentType.ConceptoFormativo -> "Concepto"
                            ContentType.ConceptoTecnico -> "Concepto"
                        }
                    }",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentTypeSelector(
    selectedType: ContentType,
    onTypeSelected: (ContentType) -> Unit
) {
    val options = mapOf(
        ContentType.Familia to "Familia",
        ContentType.ConceptoFormativo to "Concepto Formativo",
        ContentType.ConceptoTecnico to "Concepto Técnico"
    )

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEach { (type, label) ->
            SegmentedButton(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) }
            ) {
                Text(label)
            }
        }
    }
}

// --- Formularios Dinámicos ---

@Composable
fun FamilyForm() {
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
    var conceptName by remember { mutableStateOf("") }
    var conceptDescription by remember { mutableStateOf("") }

    // --- Lógica para el menú desplegable ---
    val families = listOf("Arco", "Columna") // Datos de ejemplo
    var expanded by remember { mutableStateOf(false) }
    var selectedFamily by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = conceptName,
            onValueChange = { conceptName = it },
            label = { Text("Nombre del Concepto") },
            modifier = Modifier.fillMaxWidth()
        )

        // Menú desplegable para seleccionar la familia
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            // Este TextField ahora es el "ancla" del menú y muestra la selección
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(), // Es importante decirle que es el ancla
                readOnly = true,
                value = selectedFamily,
                onValueChange = {}, // No hace nada porque es de solo lectura
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
    OutlinedButton(
        onClick = { /* Lógica para cargar imagen se implementará más adelante */ },
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

// --- Vista Previa ---
@Preview(showBackground = true)
@Composable
fun AddContentScreenPreview() {
    RestaurAppTheme {
        AddContentScreen(onNavigateBack = {})
    }
}