package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun AddConceptScreenExpanded(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
) {
    AddConceptScreenBase(
        modifier = modifier,
        vm = vm,
        onNavigateBack = onNavigateBack,
        formWidth = 0.6f
    )
}