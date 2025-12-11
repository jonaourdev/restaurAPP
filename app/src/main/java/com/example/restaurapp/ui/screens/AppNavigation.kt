// Contenido 100% corregido para AppNavigation.kt

package com.example.restaurapp.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.addConceptScreen.AddConceptScreen
import com.example.restaurapp.ui.screens.addFamilyScreeen.AddFamilyScreen
import com.example.restaurapp.ui.screens.detailConceptScreen.DetailConceptScreen
import com.example.restaurapp.ui.screens.editProfileScreen.EditProfileScreen
import com.example.restaurapp.ui.screens.familyDetailScreen.FamilyDetailScreen
import com.example.restaurapp.ui.screens.favoritesScreen.FavoriteScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreen
import com.example.restaurapp.ui.screens.listConceptScreen.ListConceptScreen
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.ui.screens.profileScreen.ProfileScreen
import com.example.restaurapp.viewmodel.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    val isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    // La vista para pantallas expandidas (tablets en horizontal)
    if (isExpandedScreen) {
        Row {
            PermanentNavigationDrawer(
                drawerContent = { /* Aquí podrías poner un menú lateral permanente */ }
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

    // --- INICIALIZACIÓN CORRECTA DE VIEWMODELS ---
    // Usamos las Factories que ya creamos para instanciar los ViewModels.
    // La función `viewModel()` se encarga de gestionar el ciclo de vida,
    // sobreviviendo a cambios de configuración y recomposiciones.
    // Esta es la forma canónica y segura de hacerlo.
    val context = LocalContext.current
    val authVm: AuthViewModel = viewModel(factory = AuthViewModelFactory.getInstance(context))
    val conceptVm: ConceptViewModel = viewModel(factory = ConceptViewModelFactory.getInstance())

    val authState by authVm.uiState.collectAsState()

    // Este efecto se encarga de refrescar los conceptos cuando el usuario cambia.
    LaunchedEffect(key1 = authState.currentUser) {
        val user = authState.currentUser
        if (user != null) {
            // Usuario logueado: recarga los conceptos con sus favoritos.
            conceptVm.refreshAllData(user.id)
        } else {
            // Invitado o logout: recarga los conceptos sin información de favoritos.
            conceptVm.refreshAllData(0)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // --- Login Screen ---
        composable(route = Screen.Login.route) {
            var isGuestLoading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            if (authState.success) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                vm = authVm,
                isGuestLoading = isGuestLoading,
                onGoRegister = { navController.navigate(Screen.Register.route) },
                onGuestAccess = {
                    scope.launch {
                        isGuestLoading = true
                        delay(2000)
                        isGuestLoading = false
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                windowSizeClass = windowSizeClass
            )
        }

        // --- Register Screen ---
        composable(route = Screen.Register.route) {
            if (authState.success) {
                LaunchedEffect(Unit) {
                    // Si el registro es exitoso, vuelve a Login para que el usuario inicie sesión.
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            RegisterScreen(
                vm = authVm,
                onGoLogin = { navController.popBackStack() },
                onRegisterClick = { authVm.registrar() },
                windowSizeClass = windowSizeClass
            )
        }

        // --- Home ---
        composable(route = Screen.Home.route) {
            HomeScreen(
                windowSizeClass = windowSizeClass,
                navController = navController,
                authVm = authVm
            )
        }

        // --- Profile Screen ---
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                windowSizeClass = windowSizeClass,
                navController = navController,
                vm = authVm,
                onGoToEdit = { navController.navigate(Screen.EditProfile.route) },
                onLogoutClick = {
                    authVm.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- Favorites ---
        composable(route = Screen.Favorites.route) {
            FavoriteScreen(
                windowSizeClass = windowSizeClass,
                navController = navController,
                vm = conceptVm,
                authVm = authVm
            )
        }

        // --- List Screen ---
        composable(
            route = Screen.ListConcept.route + "?tipo={tipo}",
            arguments = listOf(navArgument("tipo") {
                type = NavType.StringType
                defaultValue = ConceptType.FORMATIVO
            })
        ) { backStackEntry ->
            val tipo = backStackEntry.arguments?.getString("tipo") ?: ConceptType.FORMATIVO

            ListConceptScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptVm,
                tipoConcepto = tipo,
                authVm = authVm,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddConcept = {
                    val route = if (tipo == ConceptType.TECNICO) {
                        Screen.AddFamily.route
                    } else {
                        Screen.AddConcept.route + "?tipo=$tipo"
                    }
                    navController.navigate(route)
                },
                onNavigateToFamily = { familyId ->
                    navController.navigate(Screen.DetailFamily.route + "/$familyId")
                },
                onNavigateToConceptDetail = { conceptId ->
                    navController.navigate(Screen.DetailConcept.route + "/$conceptId")
                }
            )
        }

        // --- Detail Family Screen ---
        composable(
            route = Screen.DetailFamily.route + "/{familyId}",
            arguments = listOf(navArgument("familyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val familyId = backStackEntry.arguments?.getLong("familyId") ?: 0

            FamilyDetailScreen(
                windowSizeClass = windowSizeClass,
                familyId = familyId,
                vm = conceptVm,
                authVm = authVm,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddConcept = { fId ->
                    navController.navigate(Screen.AddConcept.route + "?tipo=${ConceptType.TECNICO}&familyId=$fId")
                },
                onNavigateToConceptDetail = { conceptId ->
                    navController.navigate(Screen.DetailConcept.route + "/$conceptId")
                }
            )
        }

        // --- Detail Concept Screen ---
        composable(
            route = Screen.DetailConcept.route + "/{conceptId}",
            arguments = listOf(navArgument("conceptId") { type = NavType.LongType })
        ) { backStackEntry ->
            val conceptId = backStackEntry.arguments?.getLong("conceptId") ?: 0
            DetailConceptScreen(
                windowSizeClass = windowSizeClass,
                conceptId = conceptId,
                vm = conceptVm,
                authVm = authVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Add Family Screen ---
        composable(route = Screen.AddFamily.route) {
            AddFamilyScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptVm,
                onNavigateBack = { navController.popBackStack() },
                authVm = authVm
            )
        }

        // --- Add Concept Screen ---
        composable(
            route = Screen.AddConcept.route + "?tipo={tipo}&familyId={familyId}",
            arguments = listOf(
                navArgument("tipo") {
                    type = NavType.StringType
                    defaultValue = ConceptType.FORMATIVO
                },
                navArgument("familyId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val tipo = backStackEntry.arguments?.getString("tipo") ?: ConceptType.FORMATIVO
            val familyId = backStackEntry.arguments?.getLong("familyId") ?: -1L

            // Configura el ViewModel con el tipo y familyId antes de mostrar la pantalla.
            LaunchedEffect(tipo, familyId) {
                conceptVm.onConceptTypeChange(tipo)
                conceptVm.setCurrentFamilyId(if (familyId != -1L) familyId else null)
            }

            // Limpia el familyId cuando se sale de la pantalla para evitar errores.
            DisposableEffect(Unit) {
                onDispose {
                    conceptVm.setCurrentFamilyId(null)
                }
            }

            AddConceptScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptVm,
                onNavigateBack = { navController.popBackStack() },
                authVm = authVm
            )
        }

        //--- Edit Profile Screen ---
        composable(route = Screen.EditProfile.route) {
            // Carga los datos del usuario en el formulario al entrar a la pantalla.
            LaunchedEffect(Unit) {
                authVm.cargarDatosParaEdicion()
            }
            EditProfileScreen(
                windowSizeClass = windowSizeClass,
                vm = authVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
