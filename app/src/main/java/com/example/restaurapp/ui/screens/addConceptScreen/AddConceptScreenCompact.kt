package com.example.restaurapp.ui.screens.addConceptScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurapp.viewmodel.AuthViewModel
import com.example.restaurapp.viewmodel.ConceptViewModel

@Composable
fun AddConceptScreenCompact(
    modifier: Modifier = Modifier,
    vm: ConceptViewModel,
    onNavigateBack: () -> Unit,
    authVm: AuthViewModel
) {
    AddConceptScreenBase(
        modifier = modifier,
        vm = vm,
        onNavigateBack = onNavigateBack,
        formWidth = 1.0f,
        authVm = authVm
    )
}