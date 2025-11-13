package com.example.restaurapp.ui.screens.favoritesScreen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.screens.bottomNavBarScreen.BottomNavBarScreen
import com.example.restaurapp.ui.screens.homeScreen.HomeScreenCompact
import com.example.restaurapp.ui.screens.homeScreen.HomeScreenExpanded
import com.example.restaurapp.ui.screens.homeScreen.HomeScreenMedium
import com.example.restaurapp.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    windowSizeClass: WindowSizeClass,
    navController: NavController
){
    val currentRoute = navController.currentDestination?.route

    val goToListaFormativos: () -> Unit = {
        navController.navigate(Screen.ListConcept.route + "?tipo=${ConceptType.FORMATIVO}")
    }

    val goToListaTecnicos: () -> Unit = {
        navController.navigate(Screen.ListConcept.route + "?tipo=${ConceptType.TECNICO}")
    }



    Scaffold(
        bottomBar = {
            BottomNavBarScreen(navController = navController, currentRoute = currentRoute)
        }
    ) { innerPadding ->
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                HomeScreenCompact(modifier = Modifier.padding(innerPadding),
                    onGoToListaFormativos = goToListaFormativos,
                    onGoToListaTecnicos = goToListaTecnicos)
            }
            WindowWidthSizeClass.Medium -> {
                HomeScreenMedium(modifier = Modifier.padding(innerPadding),
                    onGoToListaFormativos = goToListaFormativos,
                    onGoToListaTecnicos = goToListaTecnicos)
            }
            WindowWidthSizeClass.Expanded -> {
                HomeScreenExpanded(modifier = Modifier.padding(innerPadding),
                    onGoToListaFormativos = goToListaFormativos,
                    onGoToListaTecnicos = goToListaTecnicos)
            }
        }
    }
}