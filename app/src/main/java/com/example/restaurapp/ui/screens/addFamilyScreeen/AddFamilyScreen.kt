package com.example.restaurapp.ui.screens.addFamilyScreeen

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun AddFamilyScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            AddFamilyScreenCompact(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
        WindowWidthSizeClass.Medium -> {
            AddFamilyScreenMedium(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
        else -> { // Expanded
            AddFamilyScreenExpanded(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
    }
}