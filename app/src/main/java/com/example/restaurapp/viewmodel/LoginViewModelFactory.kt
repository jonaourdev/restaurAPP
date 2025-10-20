package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.repository.AuthRepository
import java.lang.IllegalArgumentException

/**
 * Factory para crear instancias de LoginViewModel.
 *
 * Esta clase sigue el principio de Inversión de Dependencias: no crea el
 * repositorio, sino que lo recibe como un parámetro, desacoplando la creación
 * del ViewModel de la creación de sus dependencias.
 *
 * @param repository El repositorio de autenticación que usará el LoginViewModel.
 */
class LoginViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
