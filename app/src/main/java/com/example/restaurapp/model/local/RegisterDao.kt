package com.example.restaurapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface RegisterDao {

    @Query("SELECT * FROM registros ORDER BY id DESC")
    fun observarTodos(): Flow<List<RegisterEntity>>

    @Query("SELECT * FROM registros WHERE id = :id")
    suspend fun obtenerPorId(id: Int): RegisterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(expense: RegisterEntity): Long

    @Update
    suspend fun actualizar(expense: RegisterEntity)

    @Delete
    suspend fun eliminar(expense: RegisterEntity)

    @Query("DELETE FROM registros")
    suspend fun eliminarTodos()

}