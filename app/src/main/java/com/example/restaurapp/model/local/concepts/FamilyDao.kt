package com.example.restaurapp.model.local.concepts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(family: FamilyEntity)

    @Query("SELECT * FROM families ORDER BY name ASC")
    fun getAllFamilies(): Flow<List<FamilyEntity>>

    @Update
    suspend fun actualizar(family: FamilyEntity)
}