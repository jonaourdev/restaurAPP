// en app/src/main/java/com/example/restaurapp/data/local/FamilyEntity.kt
package com.example.restaurapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "families")
data class FamilyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String
)
    