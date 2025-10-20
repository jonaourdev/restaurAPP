package com.example.restaurapp.ui.screens.loginScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenExpanded(
    vm: LoginViewModel,
    isGuestLoading: Boolean,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit){
    LoginScreenBase(
        vm = vm,
        isGuestLoading = isGuestLoading,
        onGoRegister = onGoRegister,
        onGuestAccess = onGuestAccess,
        horizontalPadding = 128.dp,
        titleStyle = MaterialTheme.typography.displaySmall,
        formFieldWithFraction = 0.5f,
        buttonWidthFraction = 0.4f,
        spaceAfterTitle = 48.dp,
        spaceAfterFields = 32.dp
    )
}