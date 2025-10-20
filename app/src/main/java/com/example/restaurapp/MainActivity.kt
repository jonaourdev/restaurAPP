// En: app/src/main/java/com/example/restaurapp/MainActivity.kt
package com.example.restaurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.ui.navigation.AppNavigation // <-- ¡Importante! Importa tu nuevo archivo.
import com.example.restaurapp.ui.theme.RestaurAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Calcula la clase de tamaño de la ventana para el diseño adaptable
            val windowSizeClass = calculateWindowSizeClass(this)

            RestaurAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Llama al Composable principal de la app, pasándole la clase de tamaño
                    RestaurApp(windowSizeClass.widthSizeClass)
                }
            }
        }
    }
}

// Composable de navegación, ahora mucho más limpio.
@Composable
fun RestaurApp(widthSizeClass: WindowWidthSizeClass) {
    // 1. Crea el controlador de navegación.
    val navController = rememberNavController()

    // 2. Llama al composable que contiene el NavHost.
    // Pásale el navController para que pueda ser utilizado.
    AppNavigation(
        navController = navController,
        widthSizeClass = widthSizeClass
    )
}
