package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.restaurapp.ui.theme.RestaurAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//                                                                     V-- CAMBIO 1: Acepta la acción de navegar --V
fun ProductScreen(
    windowSizeClass: WindowWidthSizeClass,
    onNavigateToTechnicalConcepts: () -> Unit,
    onNavigateToFormativeConcepts: () -> Unit
) {
    // --- Lógica para el estado de la barra de navegación ---
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Inicio", "Favoritos", "Perfil")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.AccountCircle)

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> {
                // CAMBIO 2: Pasa la acción a la pantalla compacta
                ProductScreenCompact(
                    modifier = Modifier.padding(innerPadding),
                    onNavigateToTechnical = onNavigateToTechnicalConcepts,
                    onNavigateToFormative = onNavigateToFormativeConcepts
                )
            }
            WindowWidthSizeClass.Medium -> {
                // CAMBIO 3: Pasa la acción a la pantalla mediana
                ProductScreenMedium(
                    modifier = Modifier.padding(innerPadding),
                    onNavigateToTechnical = onNavigateToTechnicalConcepts,
                    onNavigateToFormative = onNavigateToFormativeConcepts
                )
            }
            WindowWidthSizeClass.Expanded -> {
                // CAMBIO 4: Pasa la acción a la pantalla expandida
                ProductScreenExpanded(
                    modifier = Modifier.padding(innerPadding),
                    onNavigateToTechnical = onNavigateToTechnicalConcepts,
                    onNavigateToFormative = onNavigateToFormativeConcepts
                )
            }
        }
    }
}

// --- Vistas Previas Actualizadas ---
// Las vistas previas ahora también deben pasar el parámetro.
// Les pasamos una acción vacía {} porque en el preview no se necesita navegar.

@Preview(name = "Skeleton - Compact", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun ProductScreenCompPreview() {
    RestaurAppTheme {
        ProductScreen(
            windowSizeClass = WindowWidthSizeClass.Compact,
            onNavigateToTechnicalConcepts = {},
            onNavigateToFormativeConcepts = {}
        )
    }
}

@Preview(name = "Skeleton - Medium", showBackground = true, widthDp = 840, heightDp = 480)
@Composable
fun ProductScreenMedPreview() {
    RestaurAppTheme {
        ProductScreen(
            windowSizeClass = WindowWidthSizeClass.Medium,
            onNavigateToTechnicalConcepts = {},
            onNavigateToFormativeConcepts = {}
        )
    }
}

@Preview(name = "Skeleton - Expanded", showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
fun ProductScreenExpPreview() {
    RestaurAppTheme {
        ProductScreen(
            windowSizeClass = WindowWidthSizeClass.Expanded,
            onNavigateToTechnicalConcepts = {},
            onNavigateToFormativeConcepts = {}
        )
    }
}




