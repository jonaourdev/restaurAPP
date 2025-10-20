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
        // Limpiamos el error previo al iniciar una nueva validación
        _form.update { it.copy(error = null) }

        // --- 1. PRIMERO, LAS VALIDACIONES ---
        if (f.nombreCompleto.isBlank() || f.correo.isBlank() || f.contrasenna.isBlank()) {
            _form.update { it.copy(error = "Completa todos los campos.") }
            return@launch // Se detiene aquí, isLoading nunca fue true
        }

        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        if (!emailRegex.matches(f.correo.trim())) {
            _form.update { it.copy(error = "Ingresa un correo válido.") }
            return@launch // Se detiene aquí, isLoading nunca fue true
        }

        if (f.contrasenna.length < 8) {
            _form.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres.") }
            return@launch // Se detiene aquí, isLoading nunca fue true
        }

        if (f.contrasenna != f.confirmarContrasenna) {
            _form.update { it.copy(error = "Las contraseñas no coinciden.") }
            return@launch // Se detiene aquí, isLoading nunca fue true
        }

        // --- 2. SI TODO ES VÁLIDO, SE INICIA LA CARGA ---
        _form.update { it.copy(isLoading = true) }

        // --- 3. LÓGICA DE NEGOCIO Y DELAY ---
        try {
            // Se realiza la llamada al repositorio
            repo.register(
                nombreCompleto = f.nombreCompleto,
                correo = f.correo,
                contrasenna = f.contrasenna
            )
            // Si el registro fue exitoso en el repo, esperamos para el feedback visual
            delay(3000)
            // Finalmente, marcamos el éxito para navegar y detenemos la carga
            _form.update { it.copy(success = true, isLoading = false) }
        } catch (e: Exception) {
            // Si el repo lanza un error (ej: correo ya existe), lo mostramos y detenemos la carga
            _form.update { it.copy(error = e.message, isLoading = false) }
        }
    }
}
