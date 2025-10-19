package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.repository.RegisterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginForm(
    val correo: String = "",
    val contrasenna: String = "",
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(private val repo: RegisterRepository) : ViewModel() {
    private val _form = MutableStateFlow(LoginForm())
    val form: StateFlow<LoginForm> = _form

    fun onChangeCorreo(v: String) = _form.update { it.copy(correo = v) }
    fun onChangeContrasenna(v: String) = _form.update { it.copy(contrasenna = v) }

    fun login() = viewModelScope.launch {
        val f = _form.value
        val user = repo.obtenerPorCorreo(f.correo.lowercase())

        if (user == null || user.contrasenna != f.contrasenna) {
            _form.update { it.copy(error = "Credenciales inválidas") }
        } else {
            _form.update { it.copy(success = true, error = null) }
        }
    }
}
