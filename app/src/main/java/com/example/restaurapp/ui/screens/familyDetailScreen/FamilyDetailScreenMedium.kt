package com.example.restaurapp.ui.screens.familyDetailScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun FamilyDetailScreenMedium(
    modifier: Modifier = Modifier,
    familyId: Long,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: (Long) -> Unit,
    authVm: AuthViewModel,
) {
    FamilyDetailScreenBase(
        modifier = modifier,
        familyId = familyId,
        vm = vm,
        onNavigateBack = onNavigateBack,
        onNavigateToAddConcept = onNavigateToAddConcept,
        contentPadding = PaddingValues(24.dp),
        gridCells = GridCells.Fixed(2),
        itemSpacing = 16.dp,
        authVm = authVm
    )
}