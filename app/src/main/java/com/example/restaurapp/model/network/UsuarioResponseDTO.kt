package com.example.restaurapp.model.network

data class UsuarioResponseDTO(
    val idUsuario: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasenna: String
)