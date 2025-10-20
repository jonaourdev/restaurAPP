package com.example.restaurapp.ui.screens.homeScreen


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpanded(
    modifier: Modifier = Modifier
) {
    HomeScreenBase(
        mainPadding = 64.dp,
        titleStyle = MaterialTheme.typography.headlineLarge,
        spaceAfterSearch = 24.dp,
        spaceAfterTitle = 24.dp
    )
}