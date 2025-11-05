package com.example.restaurapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.local.AppDatabase
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.model.repository.UserRepository
import java.lang.IllegalArgumentException

/**
 * Factory para crear instancias del AuthViewModel unificado.
 *
 * Esta clase sigue el principio de Inversión de Dependencias: recibe los
 * repositorios como parámetros en lugar de crearlos, desacoplando el ViewModel
 * de la creación de sus dependencias.
 *
 * @param authRepository El repositorio para operaciones de autenticación.
 * @param userRepository El repositorio para operaciones CRUD de usuarios.
 */

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se pide crear es nuestro AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // 3. SI LO ES, CREA LA INSTANCIA PASÁNDOLE AMBOS REPOSITORIOS.
            return AuthViewModel(authRepository, userRepository) as T
        }
        // Si se pide crear otro tipo de ViewModel que esta factory no conoce, lanza un error.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object{
        fun getInstance(context: Context): AuthViewModelFactory {
            val db = AppDatabase.get(context)
            val authRepository = AuthRepository(db.userDao())
            val userRepository = UserRepository(db.userDao())
            return AuthViewModelFactory(authRepository, userRepository)
        }
    }
}
