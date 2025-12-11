package com.example.restaurapp.model.local.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun observarTodos(): Flow<List<UserEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtenerPorId(id: Int): UserEntity?
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): UserEntity?

    @Query("SELECT COUNT(*) FROM usuarios WHERE correo = :correo")
    suspend fun contarPorCorreo(correo: String): Int

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertar(register: UserEntity): Long

    @Update
    suspend fun actualizar(register: UserEntity)

    @Delete
    suspend fun eliminar(register: UserEntity)

    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodos()
}