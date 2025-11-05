package com.example.restaurapp.model.local.concepts

import androidx.room.Entity
import androidx.room.PrimaryKey



object ConceptType{
    const val FORMATIVO = "FORMATIVO"
    const val TECNICO = "TECNICO"
}

@Entity(tableName = "conceptos")
data class ConceptEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreConcepto: String,
    val descripcion: String,
    val tipo: String
)