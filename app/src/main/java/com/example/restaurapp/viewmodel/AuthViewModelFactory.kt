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
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, userRepository) as T
        }
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
