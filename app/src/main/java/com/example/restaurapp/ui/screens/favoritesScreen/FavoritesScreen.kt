package com.example.restaurapp.ui.screens.favoritesScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.screens.bottomNavBarScreen.BottomNavBarScreen
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    windowSizeClass: WindowSizeClass,
    navController: NavController,
    vm: ConceptViewModel,
    authVm: AuthViewModel
) {
    val currentRoute = navController.currentDestination?.route

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Favoritos") })
        },
        bottomBar = {
            BottomNavBarScreen(navController = navController, currentRoute = currentRoute)
        }
    ) { innerPadding ->
        val gridCells = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> GridCells.Fixed(1)
            WindowWidthSizeClass.Medium -> GridCells.Fixed(2)
            else -> GridCells.Fixed(3)
        }

        FavoritesScreenBase(
            modifier = Modifier.padding(innerPadding),
            vm = vm,
            authVm = authVm,
            onNavigateToConceptDetail = { conceptId ->
                navController.navigate("${Screen.DetailConcept.route}/$conceptId")
            },
            gridCells = gridCells
        )
    }
}
