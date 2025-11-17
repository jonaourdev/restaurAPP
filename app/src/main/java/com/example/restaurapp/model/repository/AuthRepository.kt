package com.example.restaurapp.model.repository

import android.util.Log
import com.example.restaurapp.model.local.user.UserEntity
import com.example.restaurapp.model.network.* // Importa todos los DTOs, incluyendo los de usuario

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
            // CORRECCIÓN 2: Llamar al método 'createUser' que definiremos en ApiService
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
     * Realiza el login de un usuario llamando al endpoint GET /api/v1/usuarios/{correo}.
     * Devuelve una UserEntity para ser usada en la app o null si el login falla.
     */
    suspend fun login(correo: String, contrasenna: String): UserEntity? {
        Log.d("AuthRepository", "Intentando login para: $correo")
        try {
            // CORRECCIÓN 3: Llamar al método 'getUserByEmail' que definiremos en ApiService
            val response = apiService.getUserByEmail(correo.trim())

            if (response.isSuccessful) {
                val usuarioRespuesta = response.body() // Esto es un UserResponseDTO

                // Comparamos la contraseña recibida del backend con la que ingresó el usuario.
                // ¡IMPORTANTE! Esto es inseguro. En una app real, el backend nunca debería devolver la contraseña.
                // La validación debería hacerla el propio backend.
                if (usuarioRespuesta != null && usuarioRespuesta.contrasenna == contrasenna) {
                    Log.d("AuthRepository", "Login exitoso para: ${usuarioRespuesta.correo}")
                    // Mapeamos el DTO de respuesta a nuestra entidad local (UserEntity)
                    return UserEntity(
                        id = usuarioRespuesta.idUsuario, // Usamos el id del DTO
                        nombres = usuarioRespuesta.nombres,
                        apellidos = usuarioRespuesta.apellidos,
                        correo = usuarioRespuesta.correo,
                        contrasenna = "", // NUNCA guardes la contraseña en la entidad local de la UI.
                        rol = usuarioRespuesta.rol.nombre
                    )
                } else {
                    Log.w("AuthRepository", "Contraseña incorrecta para: $correo")
                    return null // Contraseña no coincide o el cuerpo de la respuesta es nulo
                }
            } else {
                Log.w("AuthRepository", "Usuario no encontrado o error del servidor para: $correo. Código: ${response.code()}")
                return null // El usuario no existe o hubo un error del servidor
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error de red en login: ${e.message}")
            return null // Error de red (no se pudo conectar)
        }
    }
}

