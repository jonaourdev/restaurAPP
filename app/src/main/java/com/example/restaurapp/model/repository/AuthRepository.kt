package com.example.restaurapp.model.repository

import android.util.Log
import com.example.restaurapp.model.local.user.UserEntity
import com.example.restaurapp.model.network.*

class AuthRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun register(nombres: String, apellidos: String, correo: String, contrasenna: String) {
        // CORRECCIÓN: Ya no enviamos el Rol, el backend lo pone por defecto.
        val userCreateDTO = UserCreateDTO(
            nombres = nombres.trim(),
            apellidos = apellidos.trim(),
            correo = correo.trim(),
            contrasenna = contrasenna
        )

        Log.d("AuthRepository", "Registrando: $userCreateDTO")
        try {
            val response = apiService.createUser(userCreateDTO)
            if (!response.isSuccessful) {
                // Leer el error del cuerpo si es posible
                val errorBody = response.errorBody()?.string()
                throw Exception("Error al registrar: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun login(correo: String, contrasenna: String): UserEntity? {
        try {
            val credentials = LoginDTO(correo.trim(), contrasenna)
            val response = apiService.loginUser(credentials)

            if (response.isSuccessful) {
                val loginResponse = response.body()

                if (loginResponse != null) {

                    SessionManager.saveToken(loginResponse.token)

                    // Mapeo correcto de LoginResponseDTO a UserEntity local
                    return UserEntity(
                        id = loginResponse.id.toInt(), // Convertir Long a Int si tu Entity usa Int
                        nombres = loginResponse.names ?: "",
                        apellidos = loginResponse.lastNames ?: "",
                        correo = loginResponse.email,
                        contrasenna = "", // No guardamos la contraseña plana
                        rol = loginResponse.role, // El backend envía el string directo (ej: "USER")
                        photoUrl = null // El login response actual no devuelve foto, puedes ajustarlo si quieres
                    )
                }
            } else {
                throw Exception("Credenciales incorrectas")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error login: ${e.message}")
            throw Exception(e.message ?: "Error de conexión")
        }
        return null
    }
}