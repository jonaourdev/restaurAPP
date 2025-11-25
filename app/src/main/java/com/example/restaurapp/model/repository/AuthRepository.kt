package com.example.restaurapp.model.repository

import android.util.Log
import com.example.restaurapp.model.local.user.UserEntity
import com.example.restaurapp.model.network.*
import com.example.restaurapp.model.network.RetrofitClient

class AuthRepository {

    // Obtenemos la instancia del servicio de API (Retrofit)
    private val apiService: ApiService = RetrofitClient.apiService

    /**
     * Registra un nuevo usuario llamando al endpoint POST /api/v1/usuarios.
     */
    suspend fun register(nombres: String, apellidos: String, correo: String, contrasenna: String) {
        // CORRECCIÓN 1: Usar el DTO correcto que definimos: UserCreateDTO
        val userCreateDTO = UserCreateDTO(
            nombres = nombres.trim(),
            apellidos = apellidos.trim(),
            correo = correo.trim(),
            contrasenna = contrasenna,
            rol = RolDTO(nombre = "ROLE_USUARIO") // El backend espera este objeto anidado
        )

        Log.d("AuthRepository", "Intentando registrar usuario: $userCreateDTO")
        try {
            // CORRECCIÓN 2: Llamar al método 'createUser' que definimos en ApiService
            val response = apiService.createUser(userCreateDTO)
            if (!response.isSuccessful) {
                // Si el backend devuelve un error (ej. 409 Conflict si el correo ya existe), lo capturamos.
                Log.e("AuthRepository", "Error del servidor al registrar: ${response.code()} - ${response.message()}")
                throw Exception("Error del servidor al registrar: ${response.code()}")
            }
            Log.d("AuthRepository", "Registro exitoso para: ${userCreateDTO.correo}")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error de red al registrar: ${e.message}")
            // Propagamos la excepción para que el ViewModel la maneje y muestre un error en la UI.
            throw Exception("No se pudo conectar al servidor para el registro. Inténtalo más tarde.")
        }
    }

    /**
     * Realiza el login de un usuario llamando al nuevo endpoint POST /api/v1/usuarios/login.
     * Devuelve una UserEntity para ser usada en la app o null si el login falla.
     */

    suspend fun login(correo: String, contrasenna: String): Result<UserEntity> {
        Log.d("AuthRepository", "Intentando login seguro para: $correo")

        return try {
            val credentials = LoginDTO(correo.trim(), contrasenna)
            val response = apiService.loginUser(credentials)

            if (response.isSuccessful && response.body() != null) {

                val usuario = response.body()!!

                val usuarioLocal = UserEntity(
                    id = usuario.id,
                    nombres = usuario.nombres,
                    apellidos = usuario.apellidos,
                    correo = usuario.correo,
                    contrasenna = "",
                    rol = usuario.rol?.nombre ?: "ROLE_USUARIO"
                )

                Log.d("AuthRepository", "Login exitoso para: ${usuarioLocal.correo}")

                Result.success(usuarioLocal)

            } else {
                Log.w("AuthRepository", "Fallo de autenticación para $correo. Code: ${response.code()}")

                // Retornamos error SIN lanzar Exception
                Result.failure(Exception("Correo o contraseña inválidos"))
            }

        } catch (e: Exception) {
            Log.e("AuthRepository", "Error de red en login: ${e.message}")

            // Devolvemos error sin romper la app
            Result.failure(Exception("No se pudo conectar con el servidor"))
        }
    }





//    suspend fun login(correo: String, contrasenna: String): UserEntity? {
//        Log.d("AuthRepository", "Intentando login seguro para: $correo")
//        try {
//            val credentials = LoginDTO(correo.trim(), contrasenna)
//            val response = apiService.loginUser(credentials)
//
//            if (response.isSuccessful) {
//                val usuarioRespuesta = response.body() // Esto es un UserResponseDTO
//
//                if (usuarioRespuesta != null) {
//                    Log.d("AuthRepository", "Login exitoso para: ${usuarioRespuesta.correo}")
//                    // Mapeamos el DTO de respuesta a nuestra entidad local (UserEntity)
//                    return UserEntity(
//                        id = usuarioRespuesta.id, // Usamos el id del DTO
//                        nombres = usuarioRespuesta.nombres,
//                        apellidos = usuarioRespuesta.apellidos,
//                        correo = usuarioRespuesta.correo,
//                        contrasenna = "", // La contraseña ya no se maneja aquí.
//                        // 🚩 CORRECCIÓN APLICADA: Acceso seguro a 'rol' y valor por defecto si es nulo.
//                        rol = usuarioRespuesta.rol?.nombre ?: "ROLE_USUARIO"
//                    )
//                }
//            } else {
//                // Si la respuesta no es 200/201 (ej. 404 por credenciales inválidas), lanzamos una excepción.
//                Log.w("AuthRepository", "Fallo de autenticación para: $correo. Código: ${response.code()}")
//                // Lanzamos una excepción para que el ViewModel muestre un error genérico.
//                throw Exception("Correo o contraseña inválidos")
//            }
//        } catch (e: Exception) {
//            // Error de conexión u otro error inesperado.
//            // Es importante registrar la excepción original para debug.
//            Log.e("AuthRepository", "Error de red en login: ${e.message}")
//            // Propagamos una excepción más clara para el ViewModel
//            throw Exception("Error de conexión. No se pudo iniciar sesión.")
//        }
//        return null
//    }
}