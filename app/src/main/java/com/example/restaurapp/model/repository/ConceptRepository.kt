package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.concepts.ConceptDao
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptWithFavorite
import com.example.restaurapp.model.local.concepts.FamilyDao
import com.example.restaurapp.model.local.concepts.FamilyEntity
import com.example.restaurapp.model.local.relations.UserFavoriteConceptEntity
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

    fun getAllConcepts(userId: Int): Flow<List<ConceptWithFavorite>> {
        return conceptDao.getAllConceptsWithFavoriteStatus(userId)
    }

    fun getConceptsByFamily(familyId: Long, userId: Int): Flow<List<ConceptWithFavorite>> {
        return conceptDao.getConceptsByFamilyWithFavoriteStatus(familyId, userId)
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

    suspend fun addFavorite(userId: Int, conceptId: Long) {
        conceptDao.addFavorite(UserFavoriteConceptEntity(userId = userId, conceptId = conceptId))
    }

    suspend fun removeFavorite(userId: Int, conceptId: Long) {
        conceptDao.removeFavorite(UserFavoriteConceptEntity(userId = userId, conceptId = conceptId))
    }
}
