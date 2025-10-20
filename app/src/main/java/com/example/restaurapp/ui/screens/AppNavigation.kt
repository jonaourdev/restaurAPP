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
import com.example.restaurapp.model.local.AppDatabase
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.model.repository.UserRepository
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreen
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.ui.screens.profileScreen.ProfileScreen
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

    val context = LocalContext.current
    val db = AppDatabase.get(context)

    val authRepository = AuthRepository(db.userDao())
    val userRepository = UserRepository(db.userDao())

    val factory = AuthViewModelFactory(authRepository, userRepository)
    val authVm: AuthViewModel = viewModel(factory = factory)

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
                        delay(2000) // Reducido para mejor UX
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
                navController = navController
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

        // --- Edit Profile Screen ---
//        composable(route = Screen.EditProfile.route) {
//            EditProfileScreen(
//                vm = authVm,
//                onProfileUpdate = {
//                    navController.popBackStack()
//                }
//            )
//        }
    }
}
