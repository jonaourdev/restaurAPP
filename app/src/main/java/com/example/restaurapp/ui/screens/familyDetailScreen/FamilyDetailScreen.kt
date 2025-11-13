package com.example.restaurapp.ui.screens.familyDetailScreen

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun FamilyDetailScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    familyId: Long,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: (Long) -> Unit,
    authVm: AuthViewModel,
    onNavigateToConceptDetail: (conceptId: Long) -> Unit
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            FamilyDetailScreenCompact(
                authVm = authVm,
                modifier = modifier,
                familyId = familyId,
                vm = vm,
                onNavigateBack = onNavigateBack,
                onNavigateToAddConcept = onNavigateToAddConcept,
                onNavigateToConceptDetail = onNavigateToConceptDetail
            )
        }
        WindowWidthSizeClass.Medium -> {
            FamilyDetailScreenMedium(
                authVm = authVm,
                modifier = modifier,
                familyId = familyId,
                vm = vm,
                onNavigateBack = onNavigateBack,
                onNavigateToAddConcept = onNavigateToAddConcept,
                onNavigateToConceptDetail = onNavigateToConceptDetail
            )
        }
        else -> {
            FamilyDetailScreenExpanded(
                authVm = authVm,
                modifier = modifier,
                familyId = familyId,
                vm = vm,
                onNavigateBack = onNavigateBack,
                onNavigateToAddConcept = onNavigateToAddConcept,
                onNavigateToConceptDetail = onNavigateToConceptDetail
            )
        }
    }
}