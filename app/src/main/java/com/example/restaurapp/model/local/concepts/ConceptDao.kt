package com.example.restaurapp.model.local.concepts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.restaurapp.model.local.relations.UserFavoriteConceptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConceptDao {

    // --- Métodos de gestión de conceptos (pueden seguir existiendo) ---

    @Query("SELECT * FROM conceptos ORDER BY id DESC")
    fun observarTodos(): Flow<List<ConceptEntity>>

    @Query("SELECT * FROM conceptos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ConceptEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(concept: ConceptEntity): Long

    @Update
    suspend fun actualizar(concept: ConceptEntity)

    @Delete
    suspend fun eliminar(concept: ConceptEntity)

    @Query("DELETE FROM conceptos")
    suspend fun eliminarTodos()


    // --- Métodos para gestionar la relación de favoritos ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: UserFavoriteConceptEntity) // Usa el nombre de entidad correcto

    @Delete
    suspend fun removeFavorite(favorite: UserFavoriteConceptEntity) // Usa el nombre de entidad correcto


    // --- Consultas que combinan conceptos y favoritos  ---

    /**
     * Obtiene todos los conceptos y su estado de favorito para un usuario específico.
     * La anotación @Transaction es CRUCIAL para que el Flow se actualice
     * cuando cambia la tabla 'user_favorite_concepts'.
     */
    @Transaction //
    @Query("""
        SELECT *,
               (SELECT COUNT(*) FROM user_favorite_concepts
                WHERE user_favorite_concepts.conceptId = conceptos.id
                AND user_favorite_concepts.userId = :userId) > 0 AS isFavorite
        FROM conceptos
    """)
    fun getAllConceptsWithFavoriteStatus(userId: Int): Flow<List<ConceptWithFavorite>>

    /**
     * Obtiene los conceptos de una familia y su estado de favorito para un usuario.
     * También necesita @Transaction para funcionar correctamente.
     */
    @Transaction //
    @Query("""
        SELECT *,
               (SELECT COUNT(*) FROM user_favorite_concepts
                WHERE user_favorite_concepts.conceptId = conceptos.id
                AND user_favorite_concepts.userId = :userId) > 0 AS isFavorite
        FROM conceptos WHERE familyId = :familyId
    """)
    fun getConceptsByFamilyWithFavoriteStatus(familyId: Long, userId: Int): Flow<List<ConceptWithFavorite>>
}
