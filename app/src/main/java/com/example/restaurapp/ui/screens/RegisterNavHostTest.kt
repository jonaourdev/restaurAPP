package com.example.restaurapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.ui.screens.registerScreen.RegisterScreen
import com.example.restaurapp.viewmodel.RegisterViewModel

@Composable
fun RegisterNavHostTest (vm: RegisterViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "register") {
        composable("register") {
            RegisterScreen (
                vm = vm,
                onBack = {
                    vm.limpiarFormulario()
                    nav.popBackStack()
                },
                onSaved = {
                    vm.guardar()
                    nav.popBackStack()
                }
            )
        }
    }
}