package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.repository.AuthRepository
import java.lang.IllegalArgumentException

/**
 * Factory para crear instancias de RegisterViewModel.
 *
 * @param repository El repositorio de autenticación necesario para el ViewModel.
 */
class RegisterViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // --- LÓGICA DE LA FACTORY CORREGIDA ---

        // 1. Ya no se crean dependencias aquí. El 'repository' viene del constructor.

        // 2. Comprueba si la clase que se pide crear es RegisterViewModel
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            // 3. Si lo es, crea la instancia pasándole el repositorio que ya tenemos.
            return RegisterViewModel(repository) as T
        }

        // Si se pide crear otro tipo de ViewModel que esta factory no conoce, lanza un error.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
