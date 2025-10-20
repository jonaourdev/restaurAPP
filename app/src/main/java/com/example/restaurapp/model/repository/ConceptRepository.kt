package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.concepts.ConceptDao
import com.example.restaurapp.model.local.concepts.ConceptEntity
import kotlinx.coroutines.flow.Flow

class ConceptRepository(private val conceptDao: ConceptDao) {


    fun getAllConcepts(): Flow<List<ConceptEntity>> {
        return conceptDao.observarTodos()
    }

    suspend fun insert(concept: ConceptEntity) {
        conceptDao.insertar(concept)
    }

    suspend fun update(concept: ConceptEntity) {
        conceptDao.actualizar(concept)
    }

    suspend fun delete(concept: ConceptEntity) {
        conceptDao.eliminar(concept)
    }
}
