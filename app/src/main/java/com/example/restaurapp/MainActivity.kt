package com.example.restaurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.navigation.NavigationEvent
import com.example.restaurapp.navigation.Screen
import com.example.restaurapp.ui.theme.RestaurAppTheme
import com.example.restaurapp.ui.screens.homeScreen.HomeScreen
import com.example.restaurapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurAppTheme{

                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                LaunchedEffect(key1 = Unit) {
                    viewModel.navigationEvents.collectLatest { event ->
                        when(event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(it.route){
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    //NAVHOST DEFINE A QUE PANTALLAS PODEMOS NAVEGAR, AQUÍ SE DEBEN MODIFICAR A FUTURO
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        //A TRAVÉS DE ESTE CÓDIGO SE NAVEGARÁ A LAS DISTINTAS OPCIONES QUE TENDREMOS
                        composable(route = Screen.Home.route){
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    val navController = rememberNavController()
    RestaurAppTheme{
        HomeScreen(
            navController = navController
        );
    }
}