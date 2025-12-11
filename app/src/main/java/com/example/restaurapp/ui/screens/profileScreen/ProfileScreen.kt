package com.example.restaurapp.ui.screens.profileScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.restaurapp.ui.screens.bottomNavBarScreen.BottomNavBarScreen
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    windowSizeClass: WindowSizeClass,
    vm: AuthViewModel,
    onLogoutClick: () -> Unit,
    onGoToEdit: () -> Unit,
    navController: NavController
) {
    val currentRoute = navController.currentDestination?.route

    Scaffold(
        bottomBar = {
            BottomNavBarScreen(navController = navController, currentRoute = currentRoute)
        }
    ) { innerPadding ->
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                ProfileScreenCompact(
                    modifier = Modifier.padding(innerPadding),
                    vm = vm,
                    onGoToEdit = onGoToEdit,
                    onLogoutClick = onLogoutClick
                )
            }
            WindowWidthSizeClass.Medium -> {
                ProfileScreenMedium(
                    modifier = Modifier.padding(innerPadding),
                    vm = vm,
                    onGoToEdit = onGoToEdit,
                    onLogoutClick = onLogoutClick
                )
            }
            WindowWidthSizeClass.Expanded -> {
                ProfileScreenExpanded(
                    modifier = Modifier.padding(innerPadding),
                    vm = vm,
                    onGoToEdit = onGoToEdit,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}
