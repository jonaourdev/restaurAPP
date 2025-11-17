package com.example.restaurapp.model.local.relations

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.user.UserEntity

@Entity(
    tableName = "user_favorite_concepts",
    primaryKeys = ["userId", "conceptId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ConceptEntity::class,
            parentColumns = ["id"],
            childColumns = ["conceptId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFavoriteConceptEntity(
    val userId: Int,
    val conceptId: Long
)
