// Paquete corregido para mantener consistencia con el resto de la navegación.
// Asegúrate de que este archivo esté físicamente en la carpeta 'ui/navigation'.
package com.example.restaurapp.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Import para la versión mejorada y segura
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.restaurapp.model.local.user.AppDatabase
import com.example.restaurapp.ui.screen.addConceptScreen.AddContentScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptDetailScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptsScreen
import com.example.restaurapp.ui.screen.productScreen.MainScreen // <<< 1. IMPORT CORREGIDO: Apunta a MainScreen
import com.example.restaurapp.ui.screen.productScreen.TechnicalConceptsScreen
import com.example.restaurapp.viewmodel.AddContentViewModel
import com.example.restaurapp.viewmodel.AddContentViewModelFactory

/**
 * Composable que define el grafo de navegación principal de la aplicación.
 *
 * @param navController El controlador que gestiona la navegación entre pantallas.
 * @param widthSizeClass La clase de tamaño de la ventana para permitir diseños adaptables.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    widthSizeClass: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.PRODUCTS_SCREEN
    ) {
        // Pantalla principal de productos/home
        composable(AppRoutes.PRODUCTS_SCREEN) {
            // <<< 2. LLAMADA CORREGIDA: Se usa MainScreen en lugar de ProductScreen
            MainScreen(
                windowSizeClass = widthSizeClass,
                onNavigateToTechnicalConcepts = { navController.navigate(AppRoutes.TECHNICAL_CONCEPTS_SCREEN) },
                onNavigateToFormativeConcepts = { navController.navigate(AppRoutes.FORMATIVE_CONCEPTS_SCREEN) },
                onNavigateToAddContent = { navController.navigate(AppRoutes.ADD_CONTENT_SCREEN) }
            )
        }

        // Pantalla de lista de conceptos técnicos
        composable(AppRoutes.TECHNICAL_CONCEPTS_SCREEN) {
            TechnicalConceptsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Pantalla de lista de conceptos formativos
        composable(AppRoutes.FORMATIVE_CONCEPTS_SCREEN) {
            FormativeConceptsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToDetail = { conceptId ->
                    // Navega a la pantalla de detalle construyendo la ruta con el ID
                    navController.navigate("${AppRoutes.FORMATIVE_CONCEPT_DETAIL_ROUTE}/$conceptId")
                }
            )
        }

        // Pantalla de detalle de un concepto formativo (recibe un argumento)
        composable(
            route = AppRoutes.FORMATIVE_CONCEPT_DETAIL_SCREEN,
            arguments = listOf(navArgument(AppRoutes.FORMATIVE_CONCEPT_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            // Extrae el ID del concepto de los argumentos de la ruta
            val conceptId = backStackEntry.arguments?.getString(AppRoutes.FORMATIVE_CONCEPT_ID_ARG)
            FormativeConceptDetailScreen(
                conceptId = conceptId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Pantalla para añadir nuevo contenido (familias y conceptos)
        composable(AppRoutes.ADD_CONTENT_SCREEN) {
            val context = LocalContext.current
            // <<< 3. CORRECCIÓN: Se usa getDatabase() para obtener la instancia de la BD
            val db = AppDatabase.get(context)
            val familyDao = db.familyDao()
            val conceptDao = db.conceptDao()

            // Configura el ViewModel con sus dependencias usando la Factory
            val factory = AddContentViewModelFactory(familyDao, conceptDao)
            val viewModel: AddContentViewModel = viewModel(factory = factory)

            // Observa el estado del ViewModel de forma segura respecto al ciclo de vida
            val families by viewModel.families.collectAsStateWithLifecycle()

            // Renderiza la pantalla de añadir contenido, pasando el estado y los manejadores de eventos
            AddContentScreen(
                windowSizeClass = widthSizeClass,
                newFamilyName = viewModel.newFamilyName,
                onFamilyNameChange = viewModel::onFamilyNameChange,
                newFamilyDescription = viewModel.newFamilyDescription,
                onFamilyDescriptionChange = viewModel::onFamilyDescriptionChange,
                onSaveFamily = viewModel::saveNewFamily,
                newConceptName = viewModel.newConceptName,
                onConceptNameChange = viewModel::onConceptNameChange,
                newConceptDescription = viewModel.newConceptDescription,
                onConceptDescriptionChange = viewModel::onConceptDescriptionChange,
                families = families,
                onSaveFormativeConcept = viewModel::saveNewFormativeConcept,
                onSaveTechnicalConcept = { familyId ->
                    viewModel.saveNewTechnicalConcept(familyId)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
