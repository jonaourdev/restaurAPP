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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.model.local.user.AppDatabase
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreen
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.ui.screens.profileScreen.ProfileScreen
import com.example.restaurapp.ui.screens.profileScreen.ProfileScreenBase
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.AuthViewModelFactory
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
    // --- 1. CREACIÓN ÚNICA Y CENTRALIZADA DEL VIEWMODEL ---
    // Se crea una sola vez y se comparte entre todas las pantallas de autenticación.
    val context = LocalContext.current
    val db = AppDatabase.get(context)
    val authRepository = AuthRepository(db.userDao())
    val factory = AuthViewModelFactory(authRepository)
    val authVm: AuthViewModel = viewModel(factory = factory)

    // Recolectamos el estado una sola vez aquí para controlar la navegación principal.
    val authState by authVm.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // --- Login Screen ---
        composable(route = Screen.Login.route) {
            // Estado para el acceso como invitado (específico de esta pantalla)
            var isGuestLoading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            // Navega a Home si el login o registro fue exitoso.
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
            // Si el registro es exitoso, navegamos de vuelta a la pantalla de Login.
            // El LaunchedEffect en la pantalla de Login se encargará de redirigir a Home.
            if (authState.success) {
                LaunchedEffect(authState.success) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                vm = authVm, // Usamos la misma instancia del ViewModel
                onGoLogin = {
                    authVm.limpiarFormularioRegistro() // Limpia los campos del formulario
                    navController.popBackStack()
                }
            )
        }

        // --- Home ---
        composable(route = Screen.Home.route) {
            HomeScreen(
                windowSizeClass = windowSizeClass,
                navController = navController // Pasamos el NavController para la navegación interna
            )
        }

        // --- Profile Screen ---
        composable(route = Screen.Profile.route) {
            // Pasamos el mismo ViewModel para acceder a `currentUser` y `logout()`
            ProfileScreen(
                navController = navController,
                windowSizeClass = windowSizeClass,
                vm = authVm,
                onLogoutClick = {
                    // Navegamos al login y limpiamos todo el historial de navegación.
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
