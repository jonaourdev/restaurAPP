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
import com.example.restaurapp.ui.screens.editProfileScreen.EditProfileScreen
import com.example.restaurapp.ui.screens.familyDetailScreen.FamilyDetailScreen

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
    val db = AppDatabase.get(context)

    val authRepository = AuthRepository(db.userDao())
    val userRepository = UserRepository(db.userDao())
    val conceptRepository = ConceptRepository(
        db.conceptDao(),
        db.familyDao())

    val factory = AuthViewModelFactory(authRepository, userRepository)
    val authVm: AuthViewModel = viewModel(factory = factory)
    val conceptFactory = ConceptViewModelFactory(conceptRepository)

    val authState by authVm.uiState.collectAsState()

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
                onGoRegister = {
                    navController.navigate(Screen.Register.route)
                },
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
                onGoLogin = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    authVm.registrar()
                },
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
                onGoToEdit = {
                    navController.navigate(Screen.EditProfile.route)
                },
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

        // --- List Screen ---
        composable(
            route = Screen.ListConcept.route + "?tipo={tipo}",
            arguments = listOf(navArgument("tipo"){
                type = NavType.StringType
                defaultValue = ConceptType.FORMATIVO
            })
        ) { backStackEntry ->
            val conceptViewModel: ConceptViewModel = viewModel(factory = conceptFactory) // Reutiliza la factory
            val tipo = backStackEntry.arguments?.getString("tipo") ?: ConceptType.FORMATIVO

            ListConceptScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptViewModel,
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
                }
            )
        }

        // --- Detail Family Screen ---
        composable(
            route = Screen.DetailFamily.route + "/{familyId}",
            arguments = listOf(navArgument("familyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val familyId = backStackEntry.arguments?.getLong("familyId") ?: 0
            val conceptViewModel: ConceptViewModel = viewModel(factory = conceptFactory)

            FamilyDetailScreen(
                windowSizeClass = windowSizeClass,
                familyId = familyId,
                vm = conceptViewModel,
                authVm = authVm,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddConcept = { fId ->
                    // Navegamos a AddConceptScreen pasando el TIPO y el ID de la familia
                    navController.navigate(
                        Screen.AddConcept.route + "?tipo=${ConceptType.TECNICO}&familyId=$fId"
                    )
                }
            )
        }

        // --- Add Family Screen ---
        composable(
            route = Screen.AddFamily.route
        ) {
            val conceptViewModel: ConceptViewModel = viewModel(factory = conceptFactory) // Reutiliza la factory
            AddFamilyScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptViewModel,
                onNavigateBack = {navController.popBackStack()}
            )
        }



        // --- Add Concept Screen ---
        composable(
            route = Screen.AddConcept.route + "?tipo={tipo}&familyId={familyId}",
            arguments = listOf(
                navArgument("tipo"){
                    type = NavType.StringType
                    defaultValue = ConceptType.FORMATIVO},
                navArgument("familyId"){
                    type = NavType.LongType
                    defaultValue = -1L
            })
        ) { backStackEntry ->
            val conceptViewModel: ConceptViewModel = viewModel(factory = conceptFactory)
            val tipo = backStackEntry.arguments?.getString("tipo") ?: ConceptType.FORMATIVO
            val familyId = backStackEntry.arguments?.getLong("familyId") ?: -1L

            LaunchedEffect(key1 = tipo, key2 = familyId) {
                conceptViewModel.onConceptTypeChange(tipo)

                if (familyId != -1L) {
                    conceptViewModel.setCurrentFamilyId(familyId)
                } else {
                    conceptViewModel.setCurrentFamilyId(null)
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    conceptViewModel.setCurrentFamilyId(null)
                }
            }


            AddConceptScreen(
                windowSizeClass = windowSizeClass,
                vm = conceptViewModel,
                onNavigateBack = {navController.popBackStack()}
            )
        }




         //--- Edit Profile Screen ---
        composable(route = Screen.EditProfile.route) {
            EditProfileScreen(
                windowSizeClass = windowSizeClass,
                vm = authVm,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

