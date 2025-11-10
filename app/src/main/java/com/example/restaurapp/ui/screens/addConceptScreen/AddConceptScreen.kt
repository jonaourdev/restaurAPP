package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun AddConceptScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            AddConceptScreenCompact(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
        WindowWidthSizeClass.Medium -> {
            AddConceptScreenMedium(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
        else -> { // Expanded
            AddConceptScreenExpanded(
                modifier = modifier,
                vm = vm,
                onNavigateBack = onNavigateBack
            )
        }
    }
}