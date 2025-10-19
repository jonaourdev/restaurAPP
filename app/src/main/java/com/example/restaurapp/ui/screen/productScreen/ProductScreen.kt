package com.example.restaurapp.ui.screen.productScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.restaurapp.ui.theme.RestaurAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(windowSizeClass: WindowWidthSizeClass) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.History, contentDescription = "Historial") },
                    label = { Text("Historial") }
                )
            }
        }
    ) { innerPadding ->
        when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> {
                ProductScreenCompact(modifier = Modifier.padding(innerPadding))
            }
            WindowWidthSizeClass.Medium -> {
                ProductScreenMedium(modifier = Modifier.padding(innerPadding))
            }
            WindowWidthSizeClass.Expanded -> {
                ProductScreenExpanded(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

// --- Vista Previa para el caso COMPACTO ---
@Preview(name = "Skeleton - Compact", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun ProductScreenCompPreview() {
    RestaurAppTheme {
        // Le pasamos manualmente la clase de tamaño Compacta
        ProductScreen(windowSizeClass = WindowWidthSizeClass.Compact)
    }
}

/* --- Vista Previa para el caso MEDIANO ---
@Preview(name = "Skeleton - Medium", showBackground = true, widthDp = 840, heightDp = 480)
@Composable
fun ProductScreenMedPreview() {
    RestaurAppTheme {
        // Le pasamos manualmente la clase de tamaño Mediana
        ProductScreen(windowSizeClass = WindowWidthSizeClass.Medium)
    }
}*/

/* --- Vista Previa para el caso EXPANDIDO ---
@Preview(name = "Skeleton - Expanded", showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
fun ProductScreenExpPreview() {
    RestaurAppTheme {
        // Le pasamos manualmente la clase de tamaño Expandida
        ProductScreen(windowSizeClass = WindowWidthSizeClass.Expanded)
    }
}*/



