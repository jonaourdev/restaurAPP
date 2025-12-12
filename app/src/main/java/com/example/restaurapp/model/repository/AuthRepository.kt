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

                    return UserEntity(
                        id = 0,                    // por ahora no tienes id desde el backend
                        nombres = "",              // si luego el backend envía nombres, los mapeas aquí
                        apellidos = "",
                        correo = correo.trim(),    // usamos el correo que se ingresó
                        contrasenna = "",          // nunca guardes la contraseña en claro
                        rol = "",                  // puedes poner "USER" si quieres
                        photoUrl = null
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