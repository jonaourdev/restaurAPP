// en app/src/main/java/com/example/restaurapp/data/local/TechnicalConceptEntity.kt
package com.example.restaurapp.model.local.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "technical_concepts",
    foreignKeys = [
        ForeignKey(
            entity = FamilyEntity::class,
            parentColumns = ["id"],
            childColumns = ["familyId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["familyId"])]
)
data class TechnicalConceptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val familyId: Int? // El ID de la familia a la que pertenece
)
    