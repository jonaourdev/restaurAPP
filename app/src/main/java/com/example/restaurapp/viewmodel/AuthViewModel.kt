package com.example.restaurapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.user.UserEntity
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.model.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    // Campos para la pantalla de Login
    val loginCorreo: String = "",
    val loginContrasenna: String = "",

    // Campos para la pantalla de Registro
    val registerNombreCompleto: String = "",
    val registerCorreo: String = "",
    val registerContrasenna: String = "",
    val registerConfirmarContrasenna: String = "",

    // --- CAMPOS PARA EDICIÓN DE PERFIL ---
    val editNombreCompleto: String = "",
    val editCorreo: String = "",
    val editProfileImageUri: Uri? = null,
    val updateSuccess: Boolean = false,

    // Estado global compartido
    val error: String? = null,
    val success: Boolean = false, // 'success' es para login/registro
    val isLoading: Boolean = false,
    val currentUser: UserEntity? = null
)

class AuthViewModel(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // --- FUNCIONES DE LOGIN ---
    fun onLoginCorreoChange(v: String) = _uiState.update { it.copy(loginCorreo = v, error = null) }
    fun onLoginContrasennaChange(v: String) = _uiState.update { it.copy(loginContrasenna = v, error = null) }

    fun login() = viewModelScope.launch {
        val state = _uiState.value
        val user = authRepo.login(state.loginCorreo, state.loginContrasenna)

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

        _uiState.update { it.copy(isLoading = true) }
        try {
            authRepo.register(
                nombreCompleto = f.registerNombreCompleto,
                correo = f.registerCorreo,
                contrasenna = f.registerContrasenna
            )
            val newUser = authRepo.login(f.registerCorreo, f.registerContrasenna)
            delay(3000)
            _uiState.update { it.copy(success = true, isLoading = false, currentUser = newUser) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message, isLoading = false) }
        }
    }

    // --- INICIO DE LA LÓGICA DE ACTUALIZACIÓN DE PERFIL ---

    fun onEditNombreChange(v: String) = _uiState.update { it.copy(editNombreCompleto = v, error = null) }
    fun onEditCorreoChange(v: String) = _uiState.update { it.copy(editCorreo = v, error = null) }
    fun onProfileImageChange(uri: Uri){
        _uiState.update { it.copy(editProfileImageUri = uri) }
    }
    fun cargarDatosParaEdicion() {
        _uiState.value.currentUser?.let { user ->
            _uiState.update {
                it.copy(
                    editNombreCompleto = user.nombreCompleto,
                    editCorreo = user.correo,
                    editProfileImageUri = null
                )
            }
        }
    }

    fun actualizarPerfil() = viewModelScope.launch {
        val state = _uiState.value
        val currentUser = state.currentUser
        val photoUrlString = state.editProfileImageUri?.toString() ?: currentUser?.photoUrl

        if (currentUser == null) {
            _uiState.update { it.copy(error = "No se puede actualizar. No hay un usuario logeado.") }
            return@launch
        }

        // Validación
        if (state.editNombreCompleto.isBlank() || state.editCorreo.isBlank()) {
            _uiState.update { it.copy(error = "El nombre y el correo no pueden estar vacíos.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, error = null, updateSuccess = false) }

        try {
            val updatedUser = currentUser.copy(
                nombreCompleto = state.editNombreCompleto,
                correo = state.editCorreo,
                photoUrl = photoUrlString
            )

            userRepo.updateUser(updatedUser)

            delay(2000)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentUser = updatedUser,
                    updateSuccess = true
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }


    fun resetUpdateStatus() {
        _uiState.update { it.copy(updateSuccess = false, error = null) }
    }



    fun logout() {
        _uiState.value = AuthUiState()
    }

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
