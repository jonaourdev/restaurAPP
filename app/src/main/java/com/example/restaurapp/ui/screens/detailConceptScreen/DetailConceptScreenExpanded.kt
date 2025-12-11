package com.example.restaurapp.ui.screens.detailConceptScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.ConceptViewModel
import com.example.restaurapp.viewmodel.AuthViewModel

@Composable
fun DetailConceptScreenExpanded(
    modifier: Modifier = Modifier,
    conceptId: Long,
    vm: ConceptViewModel,
    authVm: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    DetailConceptScreenBase(
        modifier = modifier,
        conceptId = conceptId,
        vm = vm,
        authVm = authVm,
        onNavigateBack = onNavigateBack,
        contentPadding = 32.dp,
        imageHeight = 300.dp,
        contentWidthFraction = 0.6f
    )
}