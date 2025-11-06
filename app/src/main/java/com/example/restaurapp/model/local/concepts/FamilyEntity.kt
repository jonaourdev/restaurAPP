package com.example.restaurapp.model.local.concepts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "families")
data class FamilyEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?
)