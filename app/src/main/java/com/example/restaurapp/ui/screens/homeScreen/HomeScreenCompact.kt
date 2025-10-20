package com.example.restaurapp.ui.screens.homeScreen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompact(
    modifier: Modifier = Modifier,
) {
    HomeScreenBase(
        mainPadding = 16.dp,
        titleStyle = MaterialTheme.typography.titleLarge,
        spaceAfterSearch = 32.dp,
        spaceAfterTitle = 16.dp
    )
}