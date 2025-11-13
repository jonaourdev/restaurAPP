package com.example.restaurapp.ui.screens.detailConceptScreen

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun DetailConceptScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    conceptId: Long,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            DetailConceptScreenCompact(
                modifier,
                conceptId,
                vm,
                onNavigateBack)
        }
        WindowWidthSizeClass.Medium -> {
            DetailConceptScreenMedium(modifier,
                conceptId,
                vm,
                onNavigateBack)
        }
        else -> {
            DetailConceptScreenExpanded(modifier,
                conceptId,
                vm,
                onNavigateBack)
        }
    }
}