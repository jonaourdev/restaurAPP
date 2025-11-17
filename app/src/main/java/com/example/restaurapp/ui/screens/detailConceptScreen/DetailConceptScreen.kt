package com.example.restaurapp.ui.screens.detailConceptScreen

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun DetailConceptScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    conceptId: Long,
    vm: ConceptViewModel,
    authVm: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            DetailConceptScreenCompact(
                modifier = modifier,
                conceptId = conceptId,
                vm = vm,
                authVm = authVm, // <-- AÑADIDO: Pasa el AuthViewModel
                onNavigateBack = onNavigateBack
            )
        }
        WindowWidthSizeClass.Medium -> {
            // Asumiendo que DetailConceptScreenMedium también necesita la misma actualización
            DetailConceptScreenMedium(
                modifier = modifier,
                conceptId = conceptId,
                vm = vm,
                authVm = authVm,
                onNavigateBack = onNavigateBack
            )
        }
        else -> {
            // Asumiendo que DetailConceptScreenExpanded también necesita la misma actualización
            DetailConceptScreenExpanded(
                modifier = modifier,
                conceptId = conceptId,
                vm = vm,
                authVm = authVm, // <-- AÑADIDO: Pasa el AuthViewModel
                onNavigateBack = onNavigateBack
            )
        }
    }
}
