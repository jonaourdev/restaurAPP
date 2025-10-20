package com.example.restaurapp.ui.screens.registerScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenCompact(
    vm: AuthViewModel,
    onGoLogin: () -> Unit,
    onRegisterClick: () -> Unit
){
    RegisterScreenBase(
        vm = vm,
        onGoLogin = onGoLogin,
        onRegisterClick = onRegisterClick,
        horizontalPadding = 24.dp,
        titleStyle = MaterialTheme.typography.headlineMedium,
        formFieldWidthFraction = 0.9f,
        buttonWidthFraction = 0.7f,
        spaceAfterTitle = 16.dp,
        spaceAfterFields = 24.dp
    )
}