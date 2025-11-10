package com.example.restaurapp.ui.screens.listConceptScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun ListConceptScreenCompact(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    tipoConcepto: String,
    authVm: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: () -> Unit,
    onNavigateToFamily: (Long) -> Unit
) {
    ListConceptScreenBase(
        modifier = modifier,
        vm = vm,
        tipoConcepto = tipoConcepto,
        authVm = authVm,
        onNavigateBack = onNavigateBack,
        onNavigateToAddConcept = onNavigateToAddConcept,
        onNavigateToFamily = onNavigateToFamily,
        contentPadding = PaddingValues(16.dp),
        gridCells = GridCells.Fixed(1),
        itemSpacing = 12.dp
    )
}