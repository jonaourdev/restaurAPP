package com.example.restaurapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreenBase
import com.example.restaurapp.ui.theme.RestaurAppTheme
import com.example.restaurapp.viewmodel.RegisterViewModel
import com.example.restaurapp.viewmodel.RegisterViewModelFactory

class MainActivity : ComponentActivity() {

    // ViewModel creado con Factory (base de datos Room)
    private val vmRegister: RegisterViewModel by viewModels {
        RegisterViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurAppTheme {
                val navController = rememberNavController()

                // Control de pantallas
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    // Pantalla principal
                    composable(Screen.Home.route) {
                        HomeScreenBase(
                            titleSize = 22,
                            imageHeight = 180.dp,
                            spacing = 16.dp,
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
                            }
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
