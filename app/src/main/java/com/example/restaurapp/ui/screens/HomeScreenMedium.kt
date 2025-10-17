package com.example.restaurapp.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMedium(){
    HomeScreen(
        titleSize = 24,
        imageHeight = 200.dp,
        spacing = 24.dp
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMedium(){
    HomeScreenMedium()
}