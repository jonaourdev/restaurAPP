package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.user.UserEntity
import com.example.restaurapp.model.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. UN ESTADO ÚNICO PARA TODA LA AUTENTICACIÓN
data class AuthUiState(
    // Campos para la pantalla de Login
    val loginCorreo: String = "",
    val loginContrasenna: String = "",

    // Campos para la pantalla de Registro
    val registerNombreCompleto: String = "",
    val registerCorreo: String = "",
    val registerContrasenna: String = "",
    val registerConfirmarContrasenna: String = "",

    // Estado global compartido
    val error: String? = null,
    val success: Boolean = false,
    val isLoading: Boolean = false,
    val currentUser: UserEntity? = null
)

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // --- FUNCIONES DE LOGIN ---
    fun onLoginCorreoChange(v: String) = _uiState.update { it.copy(loginCorreo = v, error = null) }
    fun onLoginContrasennaChange(v: String) = _uiState.update { it.copy(loginContrasenna = v, error = null) }

    fun login() = viewModelScope.launch {
        val state = _uiState.value
        val user = repo.login(state.loginCorreo, state.loginContrasenna)

        if (user == null) {
            _uiState.update { it.copy(error = "Correo o contraseña inválidos.") }
        } else {
            _uiState.update { it.copy(isLoading = true, error = null, currentUser = user) }
            delay(3000)
            _uiState.update { it.copy(success = true, isLoading = false) }
        }
    }

    // --- FUNCIONES DE REGISTRO ---
    fun onRegisterNombreChange(v: String) = _uiState.update { it.copy(registerNombreCompleto = v, error = null) }
    fun onRegisterCorreoChange(v: String) = _uiState.update { it.copy(registerCorreo = v, error = null) }
    fun onRegisterContrasennaChange(v: String) = _uiState.update { it.copy(registerContrasenna = v, error = null) }
    fun onRegisterConfirmarContrasennaChange(v: String) = _uiState.update { it.copy(registerConfirmarContrasenna = v, error = null) }

    fun registrar() = viewModelScope.launch {
        val f = _uiState.value
        _uiState.update { it.copy(error = null) }

        // --- Validaciones de Registro ---
        if (f.registerNombreCompleto.isBlank() || f.registerCorreo.isBlank() || f.registerContrasenna.isBlank()) {
            _uiState.update { it.copy(error = "Completa todos los campos.") }
            return@launch
        }
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        if (!emailRegex.matches(f.registerCorreo.trim())) {
            _uiState.update { it.copy(error = "Ingresa un correo válido.") }
            return@launch
        }
        if (f.registerContrasenna.length < 8) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres.") }
            return@launch
        }
        if (f.registerContrasenna != f.registerConfirmarContrasenna) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden.") }
            return@launch
        }

        // --- Lógica de negocio si las validaciones pasan ---
        _uiState.update { it.copy(isLoading = true) }
        try {
            // 1. Realiza el registro. Esta función no devuelve nada.
            repo.register(
                nombreCompleto = f.registerNombreCompleto,
                correo = f.registerCorreo,
                contrasenna = f.registerContrasenna
            )

            // --- INICIO DE LA CORRECCIÓN ---
            // 2. Después de registrar, busca al usuario recién creado usando su correo.
            //    Asumimos que repo.login hace exactamente eso.
            val newUser = repo.login(f.registerCorreo, f.registerContrasenna)
            // --- FIN DE LA CORRECCIÓN ---

            // Después de registrar, actualizamos el usuario actual y marcamos como éxito
            delay(3000)
            // 3. Ahora sí, 'newUser' es un UserEntity y la asignación es correcta.
            _uiState.update { it.copy(success = true, isLoading = false, currentUser = newUser) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message, isLoading = false) }
        }
    }

    // --- FUNCIÓN DE LOGOUT ---
    fun logout() {
        // Al cerrar sesión, reseteamos el estado a sus valores por defecto
        _uiState.value = AuthUiState()
    }

    // --- FUNCIÓN PARA LIMPIAR FORMULARIOS ---
    fun limpiarFormularioRegistro() {
        _uiState.update {
            it.copy(
                registerNombreCompleto = "",
                registerCorreo = "",
                registerContrasenna = "",
                registerConfirmarContrasenna = "",
                error = null,
                success = false,
                isLoading = false
            )
        }
    }
}
