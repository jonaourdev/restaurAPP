package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.user.UserDao
import com.example.restaurapp.model.local.user.UserEntity

class AuthRepository (private val dao: UserDao) {

    suspend fun register(nombreCompleto: String, correo: String, contrasenna: String) {
        if (dao.contarPorCorreo(correo.trim()) > 0) {
            throw Exception("El correo electrónico ya está en uso.")
        }
        val newUser = UserEntity(
            nombreCompleto = nombreCompleto.trim(),
            correo = correo.trim(),
            contrasenna = contrasenna
        )
        dao.insertar(newUser)
    }

    suspend fun login(correo: String, contrasenna: String): UserEntity? {
        val user = dao.obtenerPorCorreo(correo.trim())

        // Comprueba si el usuario existe y si la contraseña coincide.
        return if (user != null && user.contrasenna == contrasenna) {
            user
        } else {
            null
        }
    }
}