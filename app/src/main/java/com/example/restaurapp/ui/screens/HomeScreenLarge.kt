package com.example.restaurapp.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLarge(){
    HomeScreen(
        titleSize = 28,
        imageHeight = 250.dp,
        spacing = 32.dp
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLarge(){
    HomeScreenLarge()
}