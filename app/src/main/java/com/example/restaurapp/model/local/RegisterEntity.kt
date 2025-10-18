package com.example.restaurapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros")
data class RegisterEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreCompleto: String,
    val correo: String,
    val contrasenna: String
)