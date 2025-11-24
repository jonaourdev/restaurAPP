package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

// DTO para CREAR un usuario
data class UserCreateDTO(
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("contrasenna") val contrasenna: String,
    @SerializedName("rol") val rol: RolDTO
)

data class UserResponseDTO(
    @SerializedName("idUsuario") val id: Int,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("rol") val rol: RolDTO?
)

data class RolDTO(
    @SerializedName("nombre") val nombre: String = "ROLE_USUARIO"
)

data class LoginDTO(
    @SerializedName("correo") val correo: String,
    @SerializedName("contrasenna") val contrasenna: String
)