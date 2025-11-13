package com.example.restaurapp.ui.screens.listConceptScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun ListConceptScreenExpanded(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    tipoConcepto: String,
    authVm: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddConcept: () -> Unit,
    onNavigateToFamily: (Long) -> Unit,
    onNavigateToConceptDetail: (conceptId: Long) -> Unit
) {
    ListConceptScreenBase(
        modifier = modifier,
        vm = vm,
        tipoConcepto = tipoConcepto,
        authVm = authVm,
        onNavigateBack = onNavigateBack,
        onNavigateToAddConcept = onNavigateToAddConcept,
        onNavigateToFamily = onNavigateToFamily,
        contentPadding = PaddingValues(32.dp),
        gridCells = GridCells.Adaptive(minSize = 250.dp),
        itemSpacing = 20.dp,
        onNavigateToConceptDetail = onNavigateToConceptDetail,
    )
}