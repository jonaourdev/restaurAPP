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

    // --- CAMPOS DE REGISTRO ACTUALIZADOS ---
    val registerNombres: String = "",
    val registerApellidos: String = "",
    val registerCorreo: String = "",
    val registerContrasenna: String = "",
    val registerConfirmarContrasenna: String = "",

    // --- CAMPOS DE EDICIÓN DE PERFIL ACTUALIZADOS ---
    val editNombres: String = "",
    val editApellidos: String = "",
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

    // --- FUNCIONES DE LOGIN (sin cambios) ---
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

    // --- FUNCIONES DE REGISTRO (ACTUALIZADAS) ---
    fun onRegisterNombresChange(v: String) = _uiState.update { it.copy(registerNombres = v, error = null) }
    fun onRegisterApellidosChange(v: String) = _uiState.update { it.copy(registerApellidos = v, error = null) }
    fun onRegisterCorreoChange(v: String) = _uiState.update { it.copy(registerCorreo = v, error = null) }
    fun onRegisterContrasennaChange(v: String) = _uiState.update { it.copy(registerContrasenna = v, error = null) }
    fun onRegisterConfirmarContrasennaChange(v: String) = _uiState.update { it.copy(registerConfirmarContrasenna = v, error = null) }

    fun registrar() = viewModelScope.launch {
        val f = _uiState.value
        _uiState.update { it.copy(error = null) }

        // --- Validaciones de Registro ---
        if (f.registerNombres.isBlank() || f.registerApellidos.isBlank() || f.registerCorreo.isBlank() || f.registerContrasenna.isBlank()) {
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
            // Pasamos los campos separados al repositorio
            authRepo.register(
                nombres = f.registerNombres,
                apellidos = f.registerApellidos,
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

    // --- LÓGICA DE ACTUALIZACIÓN DE PERFIL (ACTUALIZADA) ---

    fun onEditNombresChange(v: String) = _uiState.update { it.copy(editNombres = v, error = null) }
    fun onEditApellidosChange(v: String) = _uiState.update { it.copy(editApellidos = v, error = null) }
    fun onEditCorreoChange(v: String) = _uiState.update { it.copy(editCorreo = v, error = null) }
    fun onProfileImageChange(uri: Uri){
        _uiState.update { it.copy(editProfileImageUri = uri) }
    }

    fun cargarDatosParaEdicion() {
        _uiState.value.currentUser?.let { user ->
            _uiState.update {
                it.copy(
                    editNombres = user.nombres,
                    editApellidos = user.apellidos,
                    editCorreo = user.correo,
                    editProfileImageUri = null // Limpia la imagen previa al cargar
                )
            }
        }
    }

    fun actualizarPerfil() = viewModelScope.launch {
        val state = _uiState.value
        val currentUser = state.currentUser
        val photoUrlString = state.editProfileImageUri?.toString() ?: currentUser?.photoUrl

        if (currentUser == null) {
            _uiState.update { it.copy(error = "No se puede actualizar. No hay un usuario logueado.") }
            return@launch
        }

        // Validación
        if (state.editNombres.isBlank() || state.editApellidos.isBlank() || state.editCorreo.isBlank()) {
            _uiState.update { it.copy(error = "Nombres, apellidos y correo no pueden estar vacíos.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, error = null, updateSuccess = false) }

        try {
            // Actualizamos la copia del usuario con los nuevos campos
            val updatedUser = currentUser.copy(
                nombres = state.editNombres,
                apellidos = state.editApellidos,
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
                registerNombres = "",
                registerApellidos = "",
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
