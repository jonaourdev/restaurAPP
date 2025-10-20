package com.example.restaurapp.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.restaurapp.model.local.user.AppDatabase
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.model.repository.ContentRepository // <<< IMPORTA EL NUEVO REPOSITORIO
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptDetailScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptsScreen
import com.example.restaurapp.ui.screen.productScreen.TechnicalConceptsScreen
import com.example.restaurapp.ui.screens.addConceptScreen.AddContentScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreen
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.viewmodel.AddContentViewModelFactory
import com.example.restaurapp.viewmodel.LoginViewModel
import com.example.restaurapp.viewmodel.LoginViewModelFactory
import com.example.restaurapp.viewmodel.RegisterViewModel
import com.example.restaurapp.viewmodel.RegisterViewModelFactory
import com.example.restaurapp.viewmodelimport.AddContentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    val isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    if (isExpandedScreen) {
        Row {
            PermanentNavigationDrawer(
                drawerContent = { /* Contenido del drawer */ }
            ) {
                AppNavHost(navController = navController, windowSizeClass = windowSizeClass)
            }
        }
    } else {
        AppNavHost(navController = navController, windowSizeClass = windowSizeClass)
    }
}

@Composable
fun AppNavHost(navController: NavHostController, windowSizeClass: WindowSizeClass) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // --- Login Screen ---
        composable(route = Screen.Login.route) {
            val context = LocalContext.current
            // <<< ¡CORRECCIÓN CRÍTICA!
            val db = AppDatabase.getDatabase(context)
            val authRepository = AuthRepository(db.userDao())
            val factory = LoginViewModelFactory(authRepository)
            val loginVm: LoginViewModel = viewModel(factory = factory)
            val loginFormState by loginVm.form.collectAsState()
            var isGuestLoading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            if (loginFormState.success) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                vm = loginVm,
                isGuestLoading = isGuestLoading,
                onGoRegister = { navController.navigate(Screen.Register.route) },
                onGuestAccess = {
                    scope.launch {
                        isGuestLoading = true
                        delay(3000)
                        isGuestLoading = false
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // --- Register Screen ---
        composable(route = Screen.Register.route) {
            val context = LocalContext.current
            // <<< ¡CORRECCIÓN CRÍTICA!
            val db = AppDatabase.getDatabase(context)
            val authRepository = AuthRepository(db.userDao())
            val factory = RegisterViewModelFactory(authRepository)
            val registerVm: RegisterViewModel = viewModel(factory = factory)
            val registerFormState by registerVm.form.collectAsState()

            if (registerFormState.success) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route){
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                    registerVm.limpiarFormulario()
                }
            }

            RegisterScreen(
                vm = registerVm,
                onRegisterClick = { registerVm.registrar() },
                onGoLogin = {
                    registerVm.limpiarFormulario()
                    navController.popBackStack()
                }
            )
        }

        // --- Home ---
        composable(route = Screen.Home.route) {
            HomeScreen(
                windowSizeClass = windowSizeClass,
                navController = navController
            )
        }

        // --- Conceptos Técnicos ---
        composable(route = Screen.TechnicalConcept.route) {
            TechnicalConceptsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Conceptos Formativos ---
        composable(route = Screen.FormativeConcept.route) {
            FormativeConceptsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { conceptId ->
                    navController.navigate(Screen.FormativeDetail(conceptId).route)
                }
            )
        }

        // --- Detalle de Concepto Formativo ---
        composable(
            route = Screen.FormativeDetail.FULL_ROUTE,
            arguments = listOf(navArgument(Screen.FormativeDetail.ARG_CONCEPT_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val conceptId = backStackEntry.arguments?.getString(Screen.FormativeDetail.ARG_CONCEPT_ID)
            FormativeConceptDetailScreen(
                conceptId = conceptId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Pantalla para Añadir Contenido (CON LA NUEVA ARQUITECTURA) ---
        composable(route = Screen.AddContent.route) {
            val context = LocalContext.current
            // <<< ¡CORRECCIÓN CRÍTICA!
            val db = AppDatabase.getDatabase(context)
            val familyDao = db.familyDao()
            val conceptDao = db.conceptDao()

            // 1. Crea la instancia del Repositorio
            val contentRepository = ContentRepository(familyDao, conceptDao)

            // 2. Crea la Factory pasándole el Repositorio
            val factory = AddContentViewModelFactory(contentRepository)
            val viewModel: AddContentViewModel = viewModel(factory = factory)

            // 3. Recolecta el estado para pasarlo a la UI
            val families by viewModel.families.collectAsStateWithLifecycle()

            // 4. Llama a la pantalla con todos los parámetros que espera
            AddContentScreen(
                windowSizeClass = windowSizeClass.widthSizeClass,
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

        // --- Perfil ---
        composable(route = Screen.Profile.route) {
            // Aquí va tu pantalla de Perfil.
        }
    }
}
