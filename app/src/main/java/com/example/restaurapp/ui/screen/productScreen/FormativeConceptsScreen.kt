package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.foundation.layout.*import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurapp.R
import com.example.restaurapp.ui.theme.RestaurAppTheme

// --- ESTRUCTURA DE DATOS ACTUALIZADA ---
data class FormativeConcept(
    val id: String, // ID único para la navegación
    val title: String,
    val shortDescription: String, // Descripción corta para la lista
    val fullDescription: String,  // Descripción larga para el detalle
    val imageRes: Int             // Recurso de imagen para el detalle
)

// --- DATOS DE EJEMPLO ACTUALIZADOS ---
val formativeConceptsList = listOf(
    FormativeConcept(
        id = "patrimonio",
        title = "Patrimonio",
        shortDescription = "Bienes materiales o inmateriales que se conservan",
        fullDescription = "Bienes muebles e inmuebles, materiales e inmateriales de propiedad de particulares o de instituciones u organismos públicos o semipúblicos que tienen un valor excepcional desde el punto de vista de la historia, del arte, de la ciencia y de la cultura y por lo tanto sean dignos de ser considerados y conservados por la nación”. UNESCO 1977",
        imageRes = R.drawable.patrimonio
    ),
    FormativeConcept(
        id = "conservacion",
        title = "Conservación",
        shortDescription = "Conjunto de acciones preventivas para resguardar el patrimonio",
        fullDescription = "Conjunto de acciones preventivas y directas para resguardar el patrimonio para evitar o prevenir las alteraciones futuras de un bien determinado.\n" +
                "Medidas adoptadas para que un bien determinado experimente el menor número de alteraciones durante el mayor tiempo posible.\n",
        imageRes = R.drawable.conservacion
    ),
    FormativeConcept(
        id = "reversibilidad",
        title = "Reversibilidad",
        shortDescription = "Garantiza que la intervencion pueda ser revertida",
        fullDescription = "Principio que garantiza que una intervención posea capacidad de ser deshecha o retirada en el futuro sin causar daño al bien original ni comprometer su autenticidad e integridad.",
        imageRes = R.drawable.reversibilidad
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormativeConceptsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {}
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { /* ... (Barra de búsqueda sin cambios) ... */ }
            items(formativeConceptsList) { concept ->
                FormativeConceptCard(
                    concept = concept,
                    onClick = { onNavigateToDetail(concept.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormativeConceptCard(
    concept: FormativeConcept,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = concept.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
            Text(text = concept.shortDescription, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormativeConceptsScreenPreview() {
    RestaurAppTheme {
        FormativeConceptsScreen(onNavigateBack = {}, onNavigateToDetail = {})
    }
}
