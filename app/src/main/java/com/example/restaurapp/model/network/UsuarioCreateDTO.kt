package com.example.restaurapp.model.network

data class UsuarioCreateDTO(
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasenna: String
)