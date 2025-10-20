package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.repository.AuthRepository
import java.lang.IllegalArgumentException

/**
 * Factory para crear instancias del AuthViewModel unificado.
 *
 * Esta clase sigue el principio de Inversión de Dependencias: recibe el
 * repositorio como parámetro en lugar de crearlo, desacoplando el ViewModel
 * de la creación de sus dependencias.
 *
 * @param repository El repositorio de autenticación que usará el AuthViewModel.
 */
class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se pide crear es nuestro AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Si lo es, crea la instancia de AuthViewModel pasándole el repositorio.
            return AuthViewModel(repository) as T
        }
        // Si se pide crear otro tipo de ViewModel que esta factory no conoce, lanza un error.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
