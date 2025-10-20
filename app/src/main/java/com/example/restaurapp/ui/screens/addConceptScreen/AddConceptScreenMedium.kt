package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun AddContentScreenMedium(
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 64.dp, vertical = 16.dp) // El padding específico para Medium se mantiene
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Pestañas del tipo de formulario que se desea agregar
        ContentTypeSelector(
            selectedType = selectedContentType,
            onTypeSelected = onContentTypeSelected
        )

        // Tipo de formulario según la pestaña seleccionada
        when (selectedContentType) {
            // --- 2. PASAMOS EL ESTADO HACIA ABAJO A CADA FORMULARIO ---
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

@Preview(showBackground = true, widthDp = 840, heightDp = 480)
@Composable
fun AddContentScreenMediumPreview() {
    RestaurAppTheme {
        // La preview simple ya no funciona.
        // Se puede crear una preview más compleja pasándole datos falsos.
        AddContentScreenMedium(
            selectedContentType = ContentType.ConceptoTecnico,
            onContentTypeSelected = {},
            newFamilyName = "",
            onFamilyNameChange = {},
            newFamilyDescription = "",
            onFamilyDescriptionChange = {},
            onSaveFamily = {},
            newConceptName = "Columna Dórica",
            onConceptNameChange = {},
            newConceptDescription = "Una columna clásica sin basa.",
            onConceptDescriptionChange = {},
            families = listOf(
                FamilyEntity(id = 1, name = "Columnas", description = ""),
                FamilyEntity(id = 2, name = "Arcos", description = "")
            ),
            onSaveFormativeConcept = {},
            onSaveTechnicalConcept = {}
        )
    }
}
