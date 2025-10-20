package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.model.local.user.FamilyEntity
import com.example.restaurapp.ui.theme.RestaurAppTheme

@Composable
fun AddContentScreenExpanded(
    modifier: Modifier = Modifier,
    // --- 1. RECIBIMOS EL ESTADO Y LOS EVENTOS ---
    selectedContentType: ContentType,
    onContentTypeSelected: (ContentType) -> Unit,
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
    onSaveTechnicalConcept: (Int) -> Unit
) {
    // El estado local y el botón de guardado genérico se han eliminado.

    Row(modifier = modifier.padding(64.dp)) {
        // Columna Izquierda: Selector de Contenido
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContentTypeSelector(
                selectedType = selectedContentType,
                onTypeSelected = onContentTypeSelected
            )
        }

        // Columna Derecha: Formulario
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- 2. PASAMOS EL ESTADO HACIA ABAJO A CADA FORMULARIO ---
            when (selectedContentType) {
                ContentType.Familia -> FamilyForm(
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
                    families = families,
                    onSaveClick = onSaveTechnicalConcept
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
fun AddContentScreenExpandedPreview() {
    RestaurAppTheme {
        // La preview ahora necesita datos de ejemplo para funcionar.
        AddContentScreenExpanded(
            selectedContentType = ContentType.ConceptoTecnico,
            onContentTypeSelected = {},
            newFamilyName = "",
            onFamilyNameChange = {},
            newFamilyDescription = "",
            onFamilyDescriptionChange = {},
            onSaveFamily = {},
            newConceptName = "Bóveda de Cañón",
            onConceptNameChange = {},
            newConceptDescription = "Una bóveda con una sección semicircular continua.",
            onConceptDescriptionChange = {},
            families = listOf(
                FamilyEntity(id = 1, name = "Bóvedas", description = ""),
                FamilyEntity(id = 2, name = "Arcos", description = "")
            ),
            onSaveFormativeConcept = {},
            onSaveTechnicalConcept = {}
        )
    }
}
