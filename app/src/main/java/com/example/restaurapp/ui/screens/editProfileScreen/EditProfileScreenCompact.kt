package com.example.restaurapp.ui.screens.editProfileScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.AuthViewModel

@Composable
fun EditProfileScreenCompact(
    modifier: Modifier = Modifier,
    vm: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    EditProfileScreenBase(
        modifier = modifier,
        vm = vm,
        onNavigateBack = onNavigateBack,
        formWidth = 1.0f
    )
}