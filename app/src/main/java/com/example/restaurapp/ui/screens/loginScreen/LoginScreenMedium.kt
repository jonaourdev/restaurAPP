package com.example.restaurapp.ui.screens.loginScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenMedium(
    vm: LoginViewModel,
    isGuestLoading: Boolean,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit){
    LoginScreenBase(
        vm = vm,
        isGuestLoading = isGuestLoading,
        onGoRegister = onGoRegister,
        onGuestAccess = onGuestAccess,
        horizontalPadding = 64.dp,
        titleStyle = MaterialTheme.typography.headlineLarge,
        formFieldWithFraction = 0.7f,
        buttonWidthFraction = 0.6f,
        spaceAfterTitle = 36.dp,
        spaceAfterFields = 32.dp,
        spaceAfterImage = 16.dp,
        imageSize = 0.8f
    )
}