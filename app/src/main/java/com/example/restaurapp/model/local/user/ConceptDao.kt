package com.example.restaurapp.model.local.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restaurapp.model.local.user.FormativeConceptEntity
import com.example.restaurapp.model.local.user.TechnicalConceptEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object para las entidades de Conceptos.
 * Define los métodos CRUD completos para las tablas 'technical_concepts' y 'formative_concepts'.
 */
@Dao
interface ConceptDao {

    // --- CRUD para Conceptos Técnicos ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechnicalConcept(concept: TechnicalConceptEntity)

    @Query("SELECT * FROM technical_concepts ORDER BY name ASC")
    fun getAllTechnicalConcepts(): Flow<List<TechnicalConceptEntity>>

    /**
     * Obtiene todos los conceptos técnicos que pertenecen a una familia específica.
     * Esta es la consulta clave para tu lógica de negocio.
     */
    @Query("SELECT * FROM technical_concepts WHERE familyId = :familyId ORDER BY name ASC")
    fun getTechnicalConceptsByFamily(familyId: Int): Flow<List<TechnicalConceptEntity>>

    @Update
    suspend fun updateTechnicalConcept(concept: TechnicalConceptEntity)

    @Delete
    suspend fun deleteTechnicalConcept(concept: TechnicalConceptEntity)


    // --- CRUD para Conceptos Formativos ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormativeConcept(concept: FormativeConceptEntity)

    @Query("SELECT * FROM formative_concepts ORDER BY name ASC")
    fun getAllFormativeConcepts(): Flow<List<FormativeConceptEntity>>

    @Update
    suspend fun updateFormativeConcept(concept: FormativeConceptEntity)

    @Delete
    suspend fun deleteFormativeConcept(concept: FormativeConceptEntity)
}
