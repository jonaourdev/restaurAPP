package com.example.restaurapp.model.local.concepts

import androidx.room.Embedded

data class ConceptWithFavorite(
    @Embedded val concept: ConceptEntity,
    val isFavorite: Boolean
)