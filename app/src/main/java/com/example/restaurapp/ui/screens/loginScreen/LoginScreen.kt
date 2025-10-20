package com.example.restaurapp.ui.screens.loginScreen

import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: AuthViewModel,
    isGuestLoading: Boolean,
    onGoRegister: () -> Unit,
    onGuestAccess: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            LoginScreenCompact(
                vm = vm,
                isGuestLoading = isGuestLoading,
                onGoRegister = onGoRegister,
                onGuestAccess = onGuestAccess
            )
        }
        WindowWidthSizeClass.Medium -> {
            LoginScreenMedium(
                vm = vm,
                isGuestLoading = isGuestLoading,
                onGoRegister = onGoRegister,
                onGuestAccess = onGuestAccess
            )
        }
        WindowWidthSizeClass.Expanded -> {
            LoginScreenExpanded(
                vm = vm,
                isGuestLoading = isGuestLoading,
                onGoRegister = onGoRegister,
                onGuestAccess = onGuestAccess
            )
        }
    }
}
