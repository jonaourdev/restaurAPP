package com.example.restaurapp.model.local.concepts

import com.example.restaurapp.model.local.concepts.ConceptEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConceptDao {

    @Query("SELECT * FROM conceptos ORDER BY id DESC")
    fun observarTodos(): Flow<List<ConceptEntity>>

    @Query("SELECT * FROM conceptos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ConceptEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertar(concept: ConceptEntity): Long

    @Update
    suspend fun actualizar(concept: ConceptEntity)

    @Delete
    suspend fun eliminar(rconcept: ConceptEntity)

    @Query("DELETE FROM conceptos")
    suspend fun eliminarTodos()
}