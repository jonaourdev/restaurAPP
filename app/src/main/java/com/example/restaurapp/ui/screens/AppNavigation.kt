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

        composable(route = Screen.Profile.route) {
            // Aquí va tu pantalla de Perfil.
        }
    }
}
