package com.example.restaurapp.ui // O com.example.restaurapp.ui.screens.registerScreen según tu estructura final


import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import com.example.restaurapp.ui.screens.registerScreen.RegisterScreenCompact
import com.example.restaurapp.ui.screens.registerScreen.RegisterScreenExpanded
import com.example.restaurapp.ui.screens.registerScreen.RegisterScreenMedium
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onGoLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> {
            RegisterScreenCompact(
                vm = vm,
                onGoLogin = onGoLogin,
                onRegisterClick = onRegisterClick
            )
        }
        WindowWidthSizeClass.Medium -> {
            RegisterScreenMedium(
                vm = vm,
                onGoLogin = onGoLogin,
                onRegisterClick = onRegisterClick
            )
        }
        WindowWidthSizeClass.Expanded -> {
            RegisterScreenExpanded(
                vm = vm,
                onGoLogin = onGoLogin,
                onRegisterClick = onRegisterClick
            )
        }
    }
}
