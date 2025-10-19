package com.example.restaurapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun AddContentScreenExpanded(modifier: Modifier = Modifier) {
    var selectedContentType by remember { mutableStateOf(ContentType.ConceptoTecnico) }

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
                onTypeSelected = { selectedContentType = it }
            )
        }

        // Columna Derecha: Formulario y Botón
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //Tipo de formulario segun la pestaña
            when (selectedContentType) {
                ContentType.Familia -> FamilyForm()
                ContentType.ConceptoFormativo -> FormativeConceptForm()
                ContentType.ConceptoTecnico -> TechnicalConceptForm()
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Logica para botón de guardado */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Guardar ${selectedContentType.name}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
fun AddContentScreenExpandedPreview() {
    RestaurAppTheme {
        AddContentScreenExpanded()
    }
}
