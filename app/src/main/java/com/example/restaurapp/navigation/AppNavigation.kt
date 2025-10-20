// En: app/src/main/java/com/example/restaurapp/ui/navigation/AppNavigation.kt
package com.example.restaurapp.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.restaurapp.model.local.concept.AppDatabase
import com.example.restaurapp.ui.screen.addConceptScreen.AddContentScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptDetailScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptsScreen
import com.example.restaurapp.ui.screen.productScreen.ProductScreen
import com.example.restaurapp.ui.screen.productScreen.TechnicalConceptsScreen
import com.example.restaurapp.viewmodel.AddContentViewModel
import com.example.restaurapp.viewmodel.AddContentViewModelFactory

@Composable
fun AppNavigation(
    navController: NavHostController,
    widthSizeClass: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        // Usamos la constante del objeto AppRoutes como punto de partida
        startDestination = AppRoutes.PRODUCTS_SCREEN
    ) {
        composable(AppRoutes.PRODUCTS_SCREEN) {
            ProductScreen(
                windowSizeClass = widthSizeClass,
                // Usamos las constantes para la navegación
                onNavigateToTechnicalConcepts = { navController.navigate(AppRoutes.TECHNICAL_CONCEPTS_SCREEN) },
                onNavigateToFormativeConcepts = { navController.navigate(AppRoutes.FORMATIVE_CONCEPTS_SCREEN) },
                onNavigateToAddContent = { navController.navigate(AppRoutes.ADD_CONTENT_SCREEN) }
            )
        }

        composable(AppRoutes.TECHNICAL_CONCEPTS_SCREEN) {
            TechnicalConceptsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppRoutes.FORMATIVE_CONCEPTS_SCREEN) {
            FormativeConceptsScreen(
                onNavigateBack = { navController.navigateUp() },
                // Construimos la ruta dinámica de forma segura
                onNavigateToDetail = { conceptId ->
                    navController.navigate("${AppRoutes.FORMATIVE_CONCEPT_DETAIL_ROUTE}/$conceptId")
                }
            )
        }

        composable(
            // Usamos la constante para la ruta con argumento
            route = AppRoutes.FORMATIVE_CONCEPT_DETAIL_SCREEN,
            // Usamos la constante para el nombre del argumento
            arguments = listOf(navArgument(AppRoutes.FORMATIVE_CONCEPT_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            // Usamos la constante para recuperar el argumento
            val conceptId = backStackEntry.arguments?.getString(AppRoutes.FORMATIVE_CONCEPT_ID_ARG)
            FormativeConceptDetailScreen(
                conceptId = conceptId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(AppRoutes.ADD_CONTENT_SCREEN) {
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)
            val familyDao = db.familyDao()
            val conceptDao = db.conceptDao()

            val factory = AddContentViewModelFactory(familyDao, conceptDao)
            val viewModel: AddContentViewModel = viewModel(factory = factory)

            val families by viewModel.families.collectAsState()

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
