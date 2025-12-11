package com.example.restaurapp.ui.screens.editProfileScreen

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.AuthViewModel

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    vm: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        vm.cargarDatosParaEdicion()
    }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            EditProfileScreenCompact(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
        WindowWidthSizeClass.Medium -> {
            EditProfileScreenMedium(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
        else -> {
            EditProfileScreenExpanded(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
    }
}