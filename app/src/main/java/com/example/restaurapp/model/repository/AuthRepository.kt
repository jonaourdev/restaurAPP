package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.user.UserEntity
import com.example.restaurapp.model.network.ApiService
import com.example.restaurapp.model.network.RetrofitClient
import com.example.restaurapp.model.network.UsuarioCreateDTO

class AuthRepository(
) {
    // 3. Obtenemos el servicio de API de nuestro Singleton
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun register(nombres: String, apellidos: String, correo: String, contrasenna: String) {
        val usuarioDTO = UsuarioCreateDTO(
            nombres = nombres.trim(),
            apellidos = apellidos.trim(),
            correo = correo.trim(),
            contrasenna = contrasenna
        )

        try {
            val response = apiService.registrarUsuario(usuarioDTO)
            if (!response.isSuccessful) {
                throw Exception("Error al registrar: ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Error de red: ${e.message}")
        }
    }
    suspend fun login(correo: String, contrasenna: String): UserEntity? {
        try {
            val response = apiService.buscarPorCorreo(correo.trim())

            if (response.isSuccessful) {
                val usuarioRespuesta = response.body()

                if (usuarioRespuesta != null && usuarioRespuesta.contrasenna == contrasenna) {
                    return UserEntity(
                        id = usuarioRespuesta.idUsuario,
                        nombres = usuarioRespuesta.nombres,
                        apellidos = usuarioRespuesta.apellidos,
                        correo = usuarioRespuesta.correo,
                        contrasenna = usuarioRespuesta.contrasenna // ¡No guardarías esto en una app real!
                    )
                }
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }
}