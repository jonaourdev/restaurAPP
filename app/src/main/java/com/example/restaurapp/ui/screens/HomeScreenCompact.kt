package com.example.restaurapp.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompact(viewModel: MainViewModel = viewModel ()){
    HomeScreen(
        titleSize = 20,
        imageHeight = 150.dp,
        spacing = 16.dp
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCompact(){
    HomeScreenCompact()
}