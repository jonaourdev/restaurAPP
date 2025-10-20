package com.example.restaurapp.ui.screens.homeScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.restaurapp.ui.screens.bottomNavBarScreen.BottomNavBarScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    navController: NavController
){
    val currentRoute = navController.currentDestination?.route
    
    Scaffold(
        bottomBar = {
            BottomNavBarScreen(navController = navController, currentRoute = currentRoute)
        }
    ) { innerPadding ->
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                HomeScreenCompact(modifier = Modifier.padding(innerPadding))
            }
            WindowWidthSizeClass.Medium -> {
                HomeScreenMedium(modifier = Modifier.padding(innerPadding))
            }
            WindowWidthSizeClass.Expanded -> {
                HomeScreenExpanded(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}


