package com.example.restaurapp.ui.screen.addConceptScreen

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
fun AddContentScreenMedium(modifier: Modifier = Modifier) {
    var selectedContentType by remember { mutableStateOf(ContentType.ConceptoTecnico) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 64.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Pestañas del tipo de formulario que se desea agregar
        ContentTypeSelector(
            selectedType = selectedContentType,
            onTypeSelected = { selectedContentType = it }
        )
        //Tipo de formulario segun la pestaña seleccionada
        when (selectedContentType) {
            ContentType.Familia -> FamilyForm()
            ContentType.ConceptoFormativo -> FormativeConceptForm()
            ContentType.ConceptoTecnico -> TechnicalConceptForm()
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Logica para el botón de guardado */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Guardar ${selectedContentType.name}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 480)
@Composable
fun AddContentScreenMediumPreview() {
    RestaurAppTheme {
        AddContentScreenMedium()
    }
}
