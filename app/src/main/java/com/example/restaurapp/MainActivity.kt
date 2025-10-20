package com.example.restaurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.restaurapp.model.local.concept.AppDataBase
import com.example.restaurapp.ui.screen.addConceptScreen.AddContentScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptDetailScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptsScreen
import com.example.restaurapp.ui.screen.productScreen.ProductScreen
import com.example.restaurapp.ui.screen.productScreen.TechnicalConceptsScreen
import com.example.restaurapp.ui.theme.RestaurAppTheme
import com.example.restaurapp.viewmodel.AddContentViewModel
import com.example.restaurapp.viewmodel.AddContentViewModelFactory

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
fun RestaurApp(widthSizeClass: WindowWidthSizeClass) {
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
                },
                onNavigateToAddContent = {
                    navController.navigate("add_content")
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

        // --- RUTA MODIFICADA ---
        composable("add_content") {
            // 1. Obtener una instancia de la base de datos y los DAOs
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)
            val familyDao = db.familyDao()
            val conceptDao = db.conceptDao()

            // 2. Crear la Factory y luego el ViewModel
            val factory = AddContentViewModelFactory(familyDao, conceptDao)
            val viewModel: AddContentViewModel = viewModel(factory = factory)

            // 3. Recolectar la lista de familias del ViewModel para el desplegable
            val families by viewModel.families.collectAsState()

            // 4. Llamar a la UI pasándole el estado y los eventos desde el ViewModel
            AddContentScreen(
                windowSizeClass = widthSizeClass,
                // Pasamos los estados del formulario
                newFamilyName = viewModel.newFamilyName,
                onFamilyNameChange = viewModel::onFamilyNameChange,
                newFamilyDescription = viewModel.newFamilyDescription,
                onFamilyDescriptionChange = viewModel::onFamilyDescriptionChange,
                onSaveFamily = viewModel::saveNewFamily,

                newConceptName = viewModel.newConceptName,
                onConceptNameChange = viewModel::onConceptNameChange,
                newConceptDescription = viewModel.newConceptDescription,
                onConceptDescriptionChange = viewModel::onConceptDescriptionChange,

                // Pasamos la lista de familias y las funciones para guardar conceptos
                families = families,
                onSaveFormativeConcept = viewModel::saveNewFormativeConcept,
                onSaveTechnicalConcept = { familyId ->
                    viewModel.saveNewTechnicalConcept(familyId)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}