package com.example.restaurapp.ui.screens.homeScreen

import androidx.compose.runtime.Composable
import com.example.restaurapp.ui.utils.obtenerWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> HomeScreenCompact(navController = rememberNavController())
        WindowWidthSizeClass.Medium -> HomeScreenMedium(navController = rememberNavController())
        WindowWidthSizeClass.Expanded -> HomeScreenLarge(navController = rememberNavController())
    }
}