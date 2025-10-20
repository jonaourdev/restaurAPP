// en app/src/main/java/com/example/restaurapp/data/local/FormativeConceptEntity.kt
package com.example.restaurapp.model.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formative_concepts")
data class FormativeConceptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String
    // En el futuro, podrías añadir un campo imageUrl, por ejemplo:
    // val imageUrl: String? = null
)
