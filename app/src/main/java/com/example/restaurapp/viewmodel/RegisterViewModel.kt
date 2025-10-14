package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.RegisterEntity
import com.example.restaurapp.model.repository.RegisterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn

// Modelo de estado del formulario
data class FormState(
    val id: Int? = null,
    val nombreCompleto: String = "",
    val correo: String = "",
    val contrasenna: String = "",
    val error: String? = null
)

class RegisterViewModel(private val repo: RegisterRepository) : ViewModel() {

    // Estado de los registros
    val registros: StateFlow<List<RegisterEntity>> =
        repo.observarRegistros().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // fun cargarParaEditar
}