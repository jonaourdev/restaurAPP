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

// Modelo de estado del formulario de login (sin cambios)
data class LoginForm(
    val correo: String = "",
    val contrasenna: String = "",
    val error: String? = null,
    val success: Boolean = false,
    val isLoading: Boolean = false
)

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _form = MutableStateFlow(LoginForm())
    val form: StateFlow<LoginForm> = _form.asStateFlow() // Usar asStateFlow() es una buena práctica para exponerlo

    // Ingreso de datos
    fun onChangeCorreo(v: String) = _form.update { it.copy(correo = v) }
    fun onChangeContrasenna(v: String) = _form.update { it.copy(contrasenna = v) }

    // --- LÓGICA DE LOGIN ACTUALIZADA ---
    fun login() = viewModelScope.launch {
        val f = _form.value
        _form.update { it.copy(isLoading = true, error = null) }

        val user = repo.login(
            correo = f.correo,
            contrasenna = f.contrasenna
        )
        delay(3000)
        // Comprueba el resultado devuelto por el repositorio
        if (user == null) {
            // Si es null, el login falló (usuario no encontrado o contraseña incorrecta)
            _form.update { it.copy(error = "Correo o contraseña inválidos.") }
        } else {
            // Si devuelve un usuario, el login fue exitoso
            _form.update { it.copy(success = true, isLoading = false) }
        }
    }
}
