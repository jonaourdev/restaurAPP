package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.RegisterDao
import com.example.restaurapp.model.local.RegisterEntity
import kotlinx.coroutines.flow.Flow

class RegisterRepository (private val dao: RegisterDao){

    fun observarRegistros(): Flow<List<RegisterEntity>> = dao.observarTodos()

    suspend fun obtener(id: Int) = dao.obtenerPorId(id)

    suspend fun guardar(
        id: Int?,
        nombreCompleto: String,
        correo: String,
        contrasenna: String
    ){
        if (id == null || id == 0) {
            dao.insertar(
                RegisterEntity(
                    nombreCompleto = nombreCompleto.trim(),
                    correo = correo.trim(),
                    contrasenna = contrasenna
                )
            )
        } else {
            dao.actualizar(
                RegisterEntity(
                    id = id,
                    nombreCompleto = nombreCompleto.trim(),
                    correo = correo.trim(),
                    contrasenna = contrasenna
                )
            )
        }
    }

    suspend fun existeCorreo(correo: String): Boolean {
        return dao.contarPorCorreo(correo.trim()) > 0
    }

    suspend fun eliminar(register: RegisterEntity) = dao.eliminar(register)
    suspend fun eliminarTodos() = dao.eliminarTodos()
}

