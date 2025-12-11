package com.example.restaurapp.ui.screens.registerScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenExpanded(
    vm: AuthViewModel,
    onGoLogin: () -> Unit,
    onRegisterClick: () -> Unit
){
    RegisterScreenBase(
        vm = vm,
        onGoLogin = onGoLogin,
        onRegisterClick = onRegisterClick,
        horizontalPadding = 128.dp,
        titleStyle = MaterialTheme.typography.displaySmall,
        formFieldWidthFraction = 0.5f,
        buttonWidthFraction = 0.4f,
        spaceAfterTitle = 48.dp,
        spaceAfterFields = 40.dp
    )
}