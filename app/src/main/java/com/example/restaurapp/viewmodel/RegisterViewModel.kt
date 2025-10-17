package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.RegisterEntity
import com.example.restaurapp.model.repository.RegisterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Modelo de estado del formulario
data class FormRegister(
    val id: Int? = null,
    val nombreCompleto: String = "",
    val correo: String = "",
    val contrasenna: String = "",
    val error: String? = null
)

class RegisterViewModel(private val repo: RegisterRepository) : ViewModel() {

    val registros: StateFlow<List<RegisterEntity>> =
        repo.observarRegistros().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Variable de manejo de estado
    private val _form = MutableStateFlow(FormRegister())
    val form: StateFlow<FormRegister> = _form.asStateFlow()

    // Ingreso de datos
    fun limpiarFormulario() = run { _form.value = FormRegister() }
    fun onChangeNombreCompleto(v: String) = _form.update { it.copy(nombreCompleto = v) }
    fun onChangeCorreo(v: String) = _form.update { it.copy(correo = v) }
    fun onChangeContrasenna(v: String) = _form.update { it.copy(contrasenna = v) }

    // Registrar usuario
    fun guardar() = viewModelScope.launch {
        val f = _form.value
        if (f.nombreCompleto.isBlank() || f.correo.isBlank() || f.contrasenna.isBlank()) {
            _form.update { it.copy(error = "Completa todos los campos.")
                return@launch
            }
        }
        repo.guardar(f.id,f.nombreCompleto,f.correo,f.contrasenna)
    }

}