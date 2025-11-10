package com.example.restaurapp.ui.screens.listConceptScreen

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun ListConceptScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass, // Recibimos el tamaño
    vm: ConceptViewModel,
    tipoConcepto: String,
    authVm: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: () -> Unit,
    onNavigateToFamily: (Long) -> Unit
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            ListConceptScreenCompact(
                modifier = modifier,
                vm = vm,
                tipoConcepto = tipoConcepto,
                authVm = authVm,
                onNavigateBack = onNavigateBack,
                onNavigateToAddConcept = onNavigateToAddConcept,
                onNavigateToFamily = onNavigateToFamily
            )
        }
        WindowWidthSizeClass.Medium -> {
            ListConceptScreenMedium(
                modifier = modifier,
                vm = vm,
                tipoConcepto = tipoConcepto,
                authVm = authVm,
                onNavigateBack = onNavigateBack,
                onNavigateToAddConcept = onNavigateToAddConcept,
                onNavigateToFamily = onNavigateToFamily
            )
        }
        else -> { // Expanded
            ListConceptScreenExpanded(
                modifier = modifier,
                vm = vm,
                tipoConcepto = tipoConcepto,
                authVm = authVm,
                onNavigateBack = onNavigateBack,
                onNavigateToAddConcept = onNavigateToAddConcept,
                onNavigateToFamily = onNavigateToFamily
            )
        }
    }
}