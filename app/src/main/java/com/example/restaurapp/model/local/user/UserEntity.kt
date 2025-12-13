package com.example.restaurapp.model.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasenna: String,
    val photoUrl: String? = null,
    val rol: String? = "ROLE_USUARIO"
)