package com.example.restaurapp.ui.screens.detailConceptScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun DetailConceptScreenMedium(
    modifier: Modifier = Modifier,
    conceptId: Long,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit
) {
    DetailConceptScreenBase(
        modifier = modifier,
        conceptId = conceptId,
        vm = vm,
        onNavigateBack = onNavigateBack,
        contentPadding = 32.dp,
        imageHeight = 250.dp,
        contentWidthFraction = 0.8f
    )
}