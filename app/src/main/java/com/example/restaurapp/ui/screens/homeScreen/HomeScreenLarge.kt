package com.example.restaurapp.ui.screens.homeScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restaurapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLarge(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
){
    HomeScreen(
        titleSize = 28,
        imageHeight = 250.dp,
        spacing = 32.dp,
        navController = navController
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLarge(){
    val navController = rememberNavController()
    HomeScreenLarge(navController = navController)
}