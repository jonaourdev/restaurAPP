package com.example.restaurapp.model.local.concepts

import com.example.restaurapp.model.local.concepts.ConceptEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restaurapp.model.local.relations.UserFavoriteConceptEntity
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: UserFavoriteConceptEntity)

    // NUEVO: Para eliminar un favorito
    @Delete
    suspend fun removeFavorite(favorite: UserFavoriteConceptEntity)

    // CONSULTA MODIFICADA: Obtiene todos los conceptos e indica si son favoritos para un usuario
    @Query("""
        SELECT *,
               (SELECT COUNT(*) FROM user_favorite_concepts
                WHERE user_favorite_concepts.conceptId = conceptos.id
                AND user_favorite_concepts.userId = :userId) > 0 AS isFavorite
        FROM conceptos
    """)
    fun getAllConceptsWithFavoriteStatus(userId: Int): Flow<List<ConceptWithFavorite>>

    // Haz lo mismo para otras consultas, por ejemplo, para obtener los conceptos de una familia
    @Query("""
        SELECT *,
               (SELECT COUNT(*) FROM user_favorite_concepts
                WHERE user_favorite_concepts.conceptId = conceptos.id
                AND user_favorite_concepts.userId = :userId) > 0 AS isFavorite
        FROM conceptos WHERE familyId = :familyId
    """)
    fun getConceptsByFamilyWithFavoriteStatus(familyId: Long, userId: Int): Flow<List<ConceptWithFavorite>>
}