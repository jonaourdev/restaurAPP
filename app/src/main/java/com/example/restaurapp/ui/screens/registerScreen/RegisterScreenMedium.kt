package com.example.restaurapp.ui.screens.registerScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenMedium(
    vm: RegisterViewModel,
    onGoLogin: () -> Unit,
    onRegisterClick: () -> Unit
){
    RegisterScreenBase(
        vm = vm,
        onGoLogin = onGoLogin,
        onRegisterClick = onRegisterClick,
        horizontalPadding = 64.dp,
        titleStyle = MaterialTheme.typography.headlineLarge,
        formFieldWidthFraction = 0.7f,
        buttonWidthFraction = 0.5f,
        spaceAfterTitle = 32.dp,
        spaceAfterFields = 32.dp
    )
}