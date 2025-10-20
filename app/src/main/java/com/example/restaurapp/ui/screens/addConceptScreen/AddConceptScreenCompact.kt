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
fun AddContentScreenCompact(
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
    onSaveTechnicalConcept: (Int) -> Unit,
) {
    // El estado local `selectedContentType` se ha movido hacia arriba (a AddContentScreen)
    // El botón de guardado genérico se ha eliminado, cada formulario tendrá el suyo.

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Pestañas de tipo de concepto (ahora controladas desde fuera)
        ContentTypeSelector(
            selectedType = selectedContentType,
            onTypeSelected = onContentTypeSelected
        )

        // Tipo de formulario según la pestaña
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

