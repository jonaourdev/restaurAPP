package com.example.restaurapp.ui.screens

import androidx.compose.runtime.getValue // Esta importación es clave
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.restaurapp.model.local.user.AppDatabase
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.viewmodel.RegisterViewModelFactory
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.viewmodel.LoginViewModel
import com.example.restaurapp.viewmodel.RegisterViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreen
import com.example.restaurapp.viewmodel.LoginViewModelFactory
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptDetailScreen
import com.example.restaurapp.ui.screen.productScreen.FormativeConceptsScreen
import com.example.restaurapp.ui.screen.productScreen.TechnicalConceptsScreen
import com.example.restaurapp.ui.screens.addConceptScreen.AddContentScreen
import com.example.restaurapp.viewmodel.AddContentViewModel
import com.example.restaurapp.viewmodel.AddContentViewModelFactory

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
            val db = AppDatabase.get(context)
            val authRepository = AuthRepository(db.userDao())
            val factory = LoginViewModelFactory(authRepository)
            val loginVm: LoginViewModel = viewModel(factory = factory)

            // RECOLECTA EL ESTADO DE FORMA SEGURA
            val loginFormState by loginVm.form.collectAsState()

            // Estado para el acceso como invitado
            var isGuestLoading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            // USA LA NUEVA VARIABLE DE ESTADO para navegar
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
                // onLoginSuccess = { /* Ya no se necesita aquí */ }, // <-- PARÁMETRO ELIMINADO
                onGoRegister = {
                    navController.navigate(Screen.Register.route)
                },
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
            val db = AppDatabase.get(context)
            val authRepository = AuthRepository(db.userDao())
            val factory = RegisterViewModelFactory(authRepository)
            val registerVm: RegisterViewModel = viewModel(factory = factory)

            // RECOLECTA EL ESTADO DE FORMA SEGURA
            val registerFormState by registerVm.form.collectAsState()

            // USA LA NUEVA VARIABLE DE ESTADO para navegar
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
                onRegisterClick = {
                    registerVm.registrar()
                },
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

        composable(route = Screen.TechnicalConcept.route) {
            TechnicalConceptsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Pantalla de Conceptos Formativos ---
        composable(route = Screen.FormativeConcept.route) {
            FormativeConceptsScreen(
                onNavigateBack = { navController.popBackStack() },
                // Construye la ruta de detalle usando la nueva data class
                onNavigateToDetail = { conceptId ->
                    navController.navigate(Screen.FormativeDetail(conceptId).route)
                }
            )
        }

        // --- Pantalla de Detalle de Concepto Formativo (con argumento) ---
        composable(
            route = Screen.FormativeDetail.FULL_ROUTE, // Usa la ruta base con el placeholder desde el companion object
            arguments = listOf(navArgument(Screen.FormativeDetail.ARG_CONCEPT_ID) { type =
                NavType.StringType })
        ) { backStackEntry ->
            // Recupera el argumento de la ruta de forma segura
            val conceptId = backStackEntry.arguments?.getString(Screen.FormativeDetail.ARG_CONCEPT_ID)
            FormativeConceptDetailScreen(
                conceptId = conceptId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Pantalla para Añadir Contenido ---
        composable(route = Screen.AddContent.route) {
            // --- Pantalla para Añadir Contenido ---composable(route = Screen.AddContent.route) {
            val context = LocalContext.current
            // <<< ¡CORRECCIÓN CRÍTICA! Cambia esto por getDatabase()
            val db = AppDatabase.get(context)
            val familyDao = db.familyDao()
            val conceptDao = db.conceptDao()

            val factory = AddContentViewModelFactory(familyDao, conceptDao)
            val viewModel: AddContentViewModel = viewModel(factory = factory)

            // Ya no necesitas 'collectAsState' aquí si la pantalla lo hace internamente.

            // ▼▼▼ ¡AQUÍ ESTÁ LA CORRECCIÓN! ▼▼▼
            // La llamada se simplifica. Asumimos que AddContentScreen ahora
            // recibe el ViewModel directamente para manejar su estado.
            AddContentScreen(
                windowSizeClass = windowSizeClass.widthSizeClass,
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable(route = Screen.Profile.route) {
            // Aquí va tu pantalla de Perfil.
        }
    }
}
