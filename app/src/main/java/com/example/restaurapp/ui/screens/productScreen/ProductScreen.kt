package com.example.restaurapp.ui.screens.productScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.screens.navBarScreen.NavBarScreen
import com.example.restaurapp.ui.theme.RestaurAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    windowSizeClass: WindowSizeClass,
    navController: NavController) {

    val currentRoute = navController.currentDestination?.route
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    var selectedItem by remember { mutableStateOf("home") }
    
    Scaffold(
        bottomBar = {
            NavBarScreen(navController = navController, currentRoute = currentRoute)
        }
    ) { innerPadding ->
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                ProductScreenCompact(modifier = Modifier.padding(innerPadding))
            }
            WindowWidthSizeClass.Medium -> {
                ProductScreenMedium(modifier = Modifier.padding(innerPadding))
            }
            WindowWidthSizeClass.Expanded -> {
                ProductScreenExpanded(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}


