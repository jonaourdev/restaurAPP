package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.user.UserDao
import com.example.restaurapp.model.local.user.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {

    fun getAllUsers(): Flow<List<UserEntity>> = dao.observarTodos()

    suspend fun getUserById(id: Int): UserEntity? = dao.obtenerPorId(id)

    suspend fun updateUser(user: UserEntity) {
        val trimmedUser = user.copy(
            nombreCompleto = user.nombreCompleto.trim(),
            correo = user.correo.trim()
        )
        dao.actualizar(trimmedUser)
    }

    suspend fun deleteUser(user: UserEntity) {
        dao.eliminar(user)
    }

    suspend fun deleteAllUsers() {
        dao.eliminarTodos()
    }
}
