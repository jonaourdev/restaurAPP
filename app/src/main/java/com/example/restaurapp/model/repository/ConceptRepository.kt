package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.concepts.ConceptDao
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.FamilyDao
import com.example.restaurapp.model.local.concepts.FamilyEntity
import kotlinx.coroutines.flow.Flow

open class ConceptRepository(
    private val conceptDao: ConceptDao,
    private val familyDao: FamilyDao
) {


    fun getAllFamilies(): Flow<List<FamilyEntity>> = familyDao.getAllFamilies()

    suspend fun insertFamily(family: FamilyEntity) {
        familyDao.insert(family)
    }

    suspend fun updateFamily(family: FamilyEntity) {
        familyDao.actualizar(family)
    }

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
