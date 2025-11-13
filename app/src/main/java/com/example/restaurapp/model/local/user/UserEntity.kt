package com.example.restaurapp.model.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreCompleto: String,
    val correo: String,
    val contrasenna: String,
    val photoUrl: String? = null
)