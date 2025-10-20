package com.example.restaurapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object para la entidad Family.
 * Define los métodos CRUD completos para la tabla 'families'.
 */
@Dao
interface FamilyDao {

    // --- CREATE ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamily(family: FamilyEntity)

    // --- READ ---
    @Query("SELECT * FROM families ORDER BY name ASC")
    fun getAllFamilies(): Flow<List<FamilyEntity>>

    @Query("SELECT * FROM families WHERE id = :familyId")
    suspend fun getFamilyById(familyId: Int): FamilyEntity?

    // --- UPDATE ---
    @Update
    suspend fun updateFamily(family: FamilyEntity)

    // --- DELETE ---
    @Delete
    suspend fun deleteFamily(family: FamilyEntity)
}
