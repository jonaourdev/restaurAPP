// Asumo que este es el path de tu ViewModel
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

// Asegúrate de que tu data class tenga estos campos
data class LoginForm(
    val correo: String = "",
    val contrasenna: String = "",
    val error: String? = null,
    val success: Boolean = false,
    val isLoading: Boolean = false
)

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _form = MutableStateFlow(LoginForm())
    val form: StateFlow<LoginForm> = _form.asStateFlow()

    fun onChangeCorreo(v: String) = _form.update { it.copy(correo = v) }
    fun onChangeContrasenna(v: String) = _form.update { it.copy(contrasenna = v) }

    fun login() = viewModelScope.launch {
        // --- INICIO DE LA CORRECCIÓN ---

        // 1. Primero, realiza la validación en el repositorio.
        val user = repo.login(
            correo = _form.value.correo,
            contrasenna = _form.value.contrasenna
        )

        // 2. Comprueba el resultado de la validación.
        if (user == null) {
            // SI EL LOGIN FALLA: Muestra el error inmediatamente y NO actives la carga.
            _form.update { it.copy(error = "Correo o contraseña inválidos.") }
        } else {
            // SI EL LOGIN ES EXITOSO:
            // a. Activa el estado de carga para mostrar el mensaje de éxito.
            _form.update { it.copy(isLoading = true, error = null) }

            // b. Espera los 3 segundos.
            delay(3000)

            // c. Finalmente, marca el éxito para que AppNavHost navegue.
            //    También puedes poner isLoading en false aquí si quieres.
            _form.update { it.copy(success = true, isLoading = false) }
        }
        // --- FIN DE LA CORRECCIÓN ---
    }
}
