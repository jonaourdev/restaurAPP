package com.example.restaurapp.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.restaurapp.model.local.AppDatabase
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.model.repository.ConceptRepository
import com.example.restaurapp.model.repository.UserRepository
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreen
import com.example.restaurapp.ui.screens.listConceptScreen.ListConceptScreen
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.ui.screens.profileScreen.ProfileScreen
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.AuthViewModelFactory
import com.example.restaurapp.viewmodel.ConceptViewModel
import com.example.restaurapp.viewmodel.ConceptViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.ui.screens.addConceptScreen.AddConceptScreen
import com.example.restaurapp.ui.screens.addFamilyScreeen.AddFamilyScreen
import com.example.restaurapp.ui.screens.detailConceptScreen.DetailConceptScreen
import com.example.restaurapp.ui.screens.editProfileScreen.EditProfileScreen
import com.example.restaurapp.ui.screens.familyDetailScreen.FamilyDetailScreen
import com.example.restaurapp.ui.screens.favoritesScreen.FavoriteScreen

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

    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }

    // Se crean los repositorios y factories una sola vez
    val authRepository = remember { AuthRepository() }
    val userRepository = remember { UserRepository(db.userDao()) }
    val conceptRepository = remember { ConceptRepository() }

    val authFactory = remember { AuthViewModelFactory(authRepository, userRepository) }
    val conceptFactory = remember { ConceptViewModelFactory(conceptRepository) }

    // --- VIEWMODELS CENTRALIZADOS ---
    val authVm: AuthViewModel = viewModel(factory = authFactory)
    val conceptVm: ConceptViewModel = viewModel(factory = conceptFactory)

    val authState by authVm.uiState.collectAsState()

    // --- EFECTO DE CARGA Y LIMPIEZA DE DATOS (VERSIÓN FINAL) ---
    LaunchedEffect(key1 = authState.currentUser) {
        val user = authState.currentUser
        if (user != null) {
            // Si hay un usuario logueado, actualiza la lista con sus favoritos.
            conceptVm.updateUserFavorites(user.id)
        } else {
            // Si no hay usuario (logout o invitado), limpia solo los favoritos,
            // manteniendo la lista pública de conceptos.
            conceptVm.clearUserFavorites()
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
                LaunchedEffect(authState.success) {
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
                LaunchedEffect(authState.success) {
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

        // -- Favorites --
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
            arguments = listOf(navArgument("tipo"){
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
                    if (tipo == ConceptType.TECNICO) {
                        navController.navigate(Screen.AddFamily.route)
                    } else {
                        navController.navigate(Screen.AddConcept.route + "?tipo=$tipo")
                    }
                },
                onNavigateToFamily = { familyId ->
                    navController.navigate(Screen.DetailFamily.route + "/$familyId")
                },
                onNavigateToConceptDetail = { conceptId: Long ->
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
                onNavigateToConceptDetail = { conceptId: Long->
                    navController.navigate(Screen.DetailConcept.route + "/$conceptId")
                }
            )
        }

        // --- Add Family Screen ---
        composable(route = Screen.AddFamily.route) {
            AddFamilyScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Add Concept Screen ---
        composable(
            route = Screen.AddConcept.route + "?tipo={tipo}&familyId={familyId}",
            arguments = listOf(
                navArgument("tipo"){
                    type = NavType.StringType
                    defaultValue = ConceptType.FORMATIVO
                },
                navArgument("familyId"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val tipo = backStackEntry.arguments?.getString("tipo") ?: ConceptType.FORMATIVO
            val familyId = backStackEntry.arguments?.getLong("familyId") ?: -1L

            LaunchedEffect(key1 = tipo, key2 = familyId) {
                conceptVm.onConceptTypeChange(tipo)
                if (familyId != -1L) {
                    conceptVm.setCurrentFamilyId(familyId)
                } else {
                    conceptVm.setCurrentFamilyId(null)
                }
            }

            DisposableEffect(Unit) {
                onDispose { conceptVm.setCurrentFamilyId(null) }
            }

            AddConceptScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        //--- Edit Profile Screen ---
        composable(route = Screen.EditProfile.route) {
            EditProfileScreen(
                windowSizeClass = windowSizeClass,
                vm = authVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Detail Concept Screen ---
        composable(
            route = Screen.DetailConcept.route + "/{conceptId}",
            arguments = listOf(navArgument("conceptId") {type = NavType.LongType})
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
    }
}
