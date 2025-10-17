package com.example.restaurapp.ui.screens

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
fun HomeScreenCompact(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
){
    HomeScreen(
        titleSize = 20,
        imageHeight = 150.dp,
        spacing = 16.dp,
        navController = navController
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCompact(){
    val navController = rememberNavController()
    HomeScreenCompact(navController = navController)
}