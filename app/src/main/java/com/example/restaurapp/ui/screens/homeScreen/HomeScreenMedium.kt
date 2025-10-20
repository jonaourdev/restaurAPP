package com.example.restaurapp.ui.screens.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMedium(
    modifier: Modifier = Modifier,
    onNavigateToAddContent: () -> Unit
) {
    HomeScreenBase(
        mainPadding = 32.dp,
        titleStyle = MaterialTheme.typography.headlineLarge,
        spaceAfterSearch = 24.dp,
        spaceAfterTitle = 24.dp,
        onNavigateToAddContent = onNavigateToAddContent
    )
}