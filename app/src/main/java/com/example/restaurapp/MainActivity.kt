package com.example.restaurapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.loginScreen.LoginScreen
import com.example.restaurapp.ui.screens.productScreen.ProductScreen
import com.example.restaurapp.ui.theme.RestaurAppTheme
import com.example.restaurapp.viewmodel.RegisterViewModel
import com.example.restaurapp.viewmodel.RegisterViewModelFactory
import com.example.restaurapp.viewmodel.LoginViewModel
import com.example.restaurapp.viewmodel.LoginViewModelFactory



@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    // ViewModel creado con Factory (base de datos Room)
    private val vmRegister: RegisterViewModel by viewModels {
        RegisterViewModelFactory(application)
    }
    private val vmLogin: LoginViewModel by viewModels {
        LoginViewModelFactory(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurAppTheme {
                val navController = rememberNavController()
                val windowSizeClass = calculateWindowSizeClass(this)

                // Control de pantallas
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    // 🔐 LOGIN
                    composable(Screen.Login.route) {
                        LoginScreen(
                            vm = vmLogin,
                            onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            },
                            onGoRegister = {
                                navController.navigate(Screen.Register.route)
                            },
                            onGuestAccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // Pantalla principal
                    composable(Screen.Home.route) {
                        ProductScreen(
                            windowSizeClass,
                            navController = navController
                        )
                    }

                    // Pantalla de Registro
                    composable(Screen.Register.route) {
                        RegisterScreen(
                            vm = vmRegister,
                            onBack = { navController.popBackStack() },
                            onSaved = {
                                vmRegister.guardar()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Usuario registrado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            navController = navController
                        )
                    }


                    // Pantalla de Perfil *IMPLEMENTAR*
                    composable(Screen.Profile.route) {
                        Text(
                            text = "Pantalla de perfil",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}
