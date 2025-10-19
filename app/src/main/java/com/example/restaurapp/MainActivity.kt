package com.example.restaurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.restaurapp.ui.screen.productScreen.ProductScreen
import com.example.restaurapp.ui.screen.productScreen.TechnicalConceptsScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptsScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptDetailScreen
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

// Composable de navegación
@Composable
fun RestaurApp(widthSizeClass: androidx.compose.material3.windowsizeclass.WindowWidthSizeClass) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "products"
    ) {
        composable("products") {
            ProductScreen(
                windowSizeClass = widthSizeClass,
                onNavigateToTechnicalConcepts = {
                    navController.navigate("technical_concepts")
                },
                onNavigateToFormativeConcepts = {
                    navController.navigate("formative_concepts")
                }
            )
        }

        // Define la segunda pantalla (ruta "technical_concepts")
        composable("technical_concepts") {
            TechnicalConceptsScreen(
                onNavigateBack = {
                    navController.navigateUp() // Esta es la acción que vuelve atrás
                }
            )
        }
        composable("formative_concepts") {
            FormativeConceptsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToDetail = { conceptId ->
                    navController.navigate("formative_concept_detail/$conceptId")
                }
            )
        }
        composable(
            "formative_concept_detail/{conceptId}",
            arguments = listOf(navArgument("conceptId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conceptId = backStackEntry.arguments?.getString("conceptId")
            FormativeConceptDetailScreen(
                conceptId = conceptId,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}

