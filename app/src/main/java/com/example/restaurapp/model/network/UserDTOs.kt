package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

// DTO para CREAR un usuario
data class UserCreateDTO(
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("contrasenna") val contrasenna: String
)

data class UserResponseDTO(
    @SerializedName("idUsuario") val id: Int,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("fotoPerfil") val fotoPerfil: String?,
    @SerializedName("rol") val rol: String
)

data class LoginDTO(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponseDTO(
    @SerializedName("token") val token: String,
    @SerializedName("message") val message: String,
    @SerializedName("idUsuario") val id: Long,
    @SerializedName("nombres") val names: String,
    @SerializedName("apellidos") val lastNames: String,
    @SerializedName("correo") val email: String,
    @SerializedName("rol") val role: String
)

// --- REGISTRO ---

data class RegisterRequestDTO(
    @SerializedName("nombres") val names: String,
    @SerializedName("apellidos") val lastNames: String,
    @SerializedName("correo") val email: String,
    // ¡OJO AQUÍ! Tu backend en 'UsuarioCreateDTO.java' espera "contrasenna", no "password"
    @SerializedName("contrasenna") val password: String,
    @SerializedName("fotoPerfil") val profilePhoto: String? = null
)

// El backend devuelve un UsuarioResponseDTO al registrarse
data class RegisterResponseDTO(
    @SerializedName("idUsuario") val id: Long,
    @SerializedName("nombres") val names: String,
    @SerializedName("apellidos") val lastNames: String,
    @SerializedName("correo") val email: String,
    @SerializedName("fotoPerfil") val profilePhoto: String?,
    @SerializedName("rol") val role: String?
)