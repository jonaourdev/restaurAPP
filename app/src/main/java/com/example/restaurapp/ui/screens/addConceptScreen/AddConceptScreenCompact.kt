package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.ui.theme.RestaurAppTheme

@Composable
fun AddContentScreenCompact(modifier: Modifier = Modifier) {
    var selectedContentType by remember { mutableStateOf(ContentType.ConceptoTecnico) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // pestañas de tipo de concepto
        ContentTypeSelector(
            selectedType = selectedContentType,
            onTypeSelected = { selectedContentType = it }
        )

        // tipo de formulario segun la pestaña
        when (selectedContentType) {
            ContentType.Familia -> FamilyForm()
            ContentType.ConceptoFormativo -> FormativeConceptForm()
            ContentType.ConceptoTecnico -> TechnicalConceptForm()
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

        // Botón para guardar el concepto
        Button(
            onClick = { /* Agregar la logica para guardado */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Guardar ${selectedContentType.name}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "AddContent Compact")
@Composable
fun AddContentScreenCompactPreview() {
    RestaurAppTheme {
        AddContentScreenCompact()
    }
}