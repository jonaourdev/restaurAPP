package com.example.restaurapp.ui.screens.loginScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenCompact(
    vm: LoginViewModel,
    isGuestLoading: Boolean,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit){
    LoginScreenBase(
        vm = vm,
        isGuestLoading = isGuestLoading,
        onGoRegister = onGoRegister,
        onGuestAccess = onGuestAccess,
        horizontalPadding = 32.dp,
        titleStyle = MaterialTheme.typography.headlineMedium,
        formFieldWithFraction = 0.9f,
        buttonWidthFraction = 0.7f,
        spaceAfterTitle = 16.dp,
        spaceAfterFields = 24.dp,
        spaceAfterImage = 24.dp,
        imageSize = 0.8f
    )
}