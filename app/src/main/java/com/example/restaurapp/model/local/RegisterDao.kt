package com.example.restaurapp.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RegisterDao {

    @Query("SELECT * FROM registros ORDER BY id DESC")
    fun observarTodos(): Flow<List<RegisterEntity>>

    @Query("SELECT * FROM registros WHERE id = :id")
    suspend fun obtenerPorId(id: Int): RegisterEntity?

    @Query("SELECT COUNT(*) FROM registros WHERE correo = :correo")
    suspend fun contarPorCorreo(correo: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(register: RegisterEntity): Long

    @Update
    suspend fun actualizar(register: RegisterEntity)

    @Delete
    suspend fun eliminar(register: RegisterEntity)

    @Query("DELETE FROM registros")
    suspend fun eliminarTodos()
}