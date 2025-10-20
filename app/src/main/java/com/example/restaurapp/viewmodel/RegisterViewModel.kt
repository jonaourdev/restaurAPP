package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Modelo de estado del formulario
data class FormRegister(
    val id: Int? = null,
    val nombreCompleto: String = "",
    val correo: String = "",
    val contrasenna: String = "",
    val confirmarContrasenna: String = "",
    val error: String? = null,
    val success: Boolean = false,
    val isLoading: Boolean = false
)

class RegisterViewModel(private val repo: AuthRepository) : ViewModel() {

    // Estado del formulario
    private val _form = MutableStateFlow(FormRegister())
    val form: StateFlow<FormRegister> = _form.asStateFlow()

    // Ingreso de datos
    fun limpiarFormulario() = run { _form.value = FormRegister() }
    fun onChangeNombreCompleto(v: String) = _form.update { it.copy(nombreCompleto = v) }
    fun onChangeCorreo(v: String) = _form.update { it.copy(correo = v) }
    fun onChangeContrasenna(v: String) = _form.update { it.copy(contrasenna = v) }
    fun onChangeConfirmarContrasenna(v: String) = _form.update { it.copy(confirmarContrasenna = v) }

    // Registrar usuario con validaciones
    fun registrar() = viewModelScope.launch {
        val f = _form.value

        _form.update { it.copy(isLoading = true, error = null) }
        delay(3000)

        // --- Validaciones de UI  ---
        if (f.nombreCompleto.isBlank() || f.correo.isBlank() || f.contrasenna.isBlank()) {
            _form.update { it.copy(error = "Completa todos los campos.") }
            return@launch
        }

        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        if (!emailRegex.matches(f.correo.trim())) {
            _form.update { it.copy(error = "Ingresa un correo válido.") }
            return@launch
        }

        if (f.contrasenna.length < 8) {
            _form.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres.") }
            return@launch
        }

        if (f.contrasenna != f.confirmarContrasenna) {
            _form.update { it.copy(error = "Las contraseñas no coinciden.") }
            return@launch
        }

        // --- Lógica de negocio delegada al repositorio ---
        try {
            repo.register(
                nombreCompleto = f.nombreCompleto,
                correo = f.correo,
                contrasenna = f.contrasenna
            )
            _form.update { it.copy(success = true) }
        } catch (e: Exception) {
            // Captura la excepción del repositorio
            _form.update { it.copy(error = e.message) }
        }
    }
}
