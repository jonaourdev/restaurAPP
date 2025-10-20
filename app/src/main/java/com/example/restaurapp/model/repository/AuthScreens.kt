// En: ui/screens/AuthScreens.kt

package com.example.restaurapp.ui.screens

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
import com.example.restaurapp.model.local.user.AppDatabase
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.viewmodel.LoginViewModel
import com.example.restaurapp.viewmodel.LoginViewModelFactory
import com.example.restaurapp.viewmodel.RegisterViewModel
import com.example.restaurapp.viewmodel.RegisterViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- Contenedor para la Pantalla de Login ---
@Composable
fun LoginRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    // 1. TODA LA LÓGICA DE CREACIÓN ESTÁ AQUÍ AHORA
    val context = LocalContext.current
    val db = AppDatabase.get(context)
    val authRepository = AuthRepository(db.userDao())
    val factory = LoginViewModelFactory(authRepository)
    val loginVm: LoginViewModel = viewModel(factory = factory)

    // 2. LA LÓGICA DE ESTADO ESTÁ AQUÍ
    val loginFormState by loginVm.form.collectAsState()
    var isGuestLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 3. LA LÓGICA DE NAVEGACIÓN SE ACTIVA COMO REACCIÓN AL ESTADO
    if (loginFormState.success) {
        // Ejecuta el efecto de navegación cuando el login es exitoso
        LaunchedEffect(Unit) {
            onNavigateToHome()
        }
    }

    // 4. SE LLAMA A LA PANTALLA DE UI PURA
    LoginScreen(
        vm = loginVm,
        isGuestLoading = isGuestLoading,
        onGoRegister = onNavigateToRegister, // Se pasa la función de navegación recibida
        onGuestAccess = {
            scope.launch {
                isGuestLoading = true
                delay(3000)
                isGuestLoading = false
                onNavigateToHome() // Se usa la función de navegación para invitados
            }
        }
    )
}

// --- Contenedor para la Pantalla de Registro ---
@Composable
fun RegisterRoute(
    onNavigateToLogin: () -> Unit,
) {
    val context = LocalContext.current
    val db = AppDatabase.get(context)
    val authRepository = AuthRepository(db.userDao())
    val factory = RegisterViewModelFactory(authRepository)
    val registerVm: RegisterViewModel = viewModel(factory = factory)
    val registerFormState by registerVm.form.collectAsState()

    if (registerFormState.success) {
        LaunchedEffect(Unit) {
            onNavigateToLogin()
            registerVm.limpiarFormulario()
        }
    }

    RegisterScreen(
        vm = registerVm,
        onRegisterClick = { registerVm.registrar() },
        onGoLogin = {
            registerVm.limpiarFormulario()
            onNavigateToLogin()
        }
    )
}
