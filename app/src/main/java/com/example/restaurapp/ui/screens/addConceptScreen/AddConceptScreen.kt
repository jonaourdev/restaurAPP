package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R
import com.example.restaurapp.model.local.user.FamilyEntity

// El Enum se queda igual
enum class ContentType {
    Familia,
    ConceptoFormativo,
    ConceptoTecnico
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(
    // 1. AÑADIMOS TODOS LOS PARÁMETROS DEL VIEWMODEL
    windowSizeClass: WindowWidthSizeClass,
    newFamilyName: String,
    onFamilyNameChange: (String) -> Unit,
    newFamilyDescription: String,
    onFamilyDescriptionChange: (String) -> Unit,
    onSaveFamily: () -> Unit,
    newConceptName: String,
    onConceptNameChange: (String) -> Unit,
    newConceptDescription: String,
    onConceptDescriptionChange: (String) -> Unit,
    families: List<FamilyEntity>,
    onSaveFormativeConcept: () -> Unit,
    onSaveTechnicalConcept: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Estado para controlar qué formulario se muestra
    var selectedContentType by remember { mutableStateOf(ContentType.Familia) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Contenido", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) }
            )
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Para que sea deslizable si el contenido no cabe
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Selector de tipo de contenido
            ContentTypeSelector(
                selectedType = selectedContentType,
                onTypeSelected = { selectedContentType = it }
            )

            // Contenido dinámico según la selección
            when (selectedContentType) {
                ContentType.Familia -> FamilyForm(
                    // 2. PASAMOS EL ESTADO Y LOS EVENTOS AL FORMULARIO
                    name = newFamilyName,
                    onNameChange = onFamilyNameChange,
                    description = newFamilyDescription,
                    onDescriptionChange = onFamilyDescriptionChange,
                    onSaveClick = onSaveFamily
                )
                ContentType.ConceptoFormativo -> FormativeConceptForm(
                    name = newConceptName,
                    onNameChange = onConceptNameChange,
                    description = newConceptDescription,
                    onDescriptionChange = onConceptDescriptionChange,
                    onSaveClick = onSaveFormativeConcept
                )
                ContentType.ConceptoTecnico -> TechnicalConceptForm(
                    name = newConceptName,
                    onNameChange = onConceptNameChange,
                    description = newConceptDescription,
                    onDescriptionChange = onConceptDescriptionChange,
                    families = families, // La lista de familias del ViewModel
                    onSaveClick = onSaveTechnicalConcept
                )
            }
        }
    }
}


// --- Componentes de Formulario Reutilizables (AHORA RECIBEN ESTADO) ---

@Composable
fun FamilyForm(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    // 3. EL FORMULARIO YA NO TIENE ESTADO PROPIO, LO RECIBE DE FUERA
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre de la Familia") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción de la Familia") },
            modifier = Modifier.fillMaxWidth()
        )
        ImageUploadButton()
        Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar Familia")
        }
    }
}

@Composable
fun FormativeConceptForm(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre del Concepto") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        ImageUploadButton()
        Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar Concepto Formativo")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicalConceptForm(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    families: List<FamilyEntity>, // Recibe la lista de entidades
    onSaveClick: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // Estado para guardar la familia seleccionada (entidad completa)
    var selectedFamily by remember { mutableStateOf<FamilyEntity?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
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
                value = selectedFamily?.name ?: "", // Muestra el nombre de la familia
                onValueChange = {},
                label = { Text("Familia Perteneciente") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (families.isEmpty()) {
                    DropdownMenuItem(text = { Text("No hay familias creadas") }, onClick = {}, enabled = false)
                }
                families.forEach { family ->
                    DropdownMenuItem(
                        text = { Text(family.name) },
                        onClick = {
                            selectedFamily = family // Guarda la entidad completa
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        ImageUploadButton()
        Button(
            onClick = { selectedFamily?.id?.let { onSaveClick(it) } }, // Llama a onSaveClick con el ID
            enabled = selectedFamily != null, // El botón solo se activa si se ha elegido una familia
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Concepto Técnico")
        }
    }
}


// --- OTROS COMPONENTES (sin cambios) ---

@Composable
fun ImageUploadButton() {
    OutlinedButton(
        onClick = { /* TODO: Lógica de carga */ },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentTypeSelector(
    selectedType: ContentType,
    onTypeSelected: (ContentType) -> Unit
) {
    val tabOptions = listOf(ContentType.Familia, ContentType.ConceptoFormativo, ContentType.ConceptoTecnico)
    val selectedTabIndex = tabOptions.indexOf(selectedType)

    TabRow(selectedTabIndex = selectedTabIndex) {
        tabOptions.forEach { type ->
            Tab(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                text = { Text(type.name) }
            )
        }
    }
}


