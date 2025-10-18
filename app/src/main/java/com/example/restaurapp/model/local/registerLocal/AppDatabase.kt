package com.example.restaurapp.model.local.registerLocal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restaurapp.model.local.registerLocal.RegisterDao
import com.example.restaurapp.model.local.registerLocal.RegisterEntity

@Database(entities = [RegisterEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registerDao(): RegisterDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "registros.db"
                ).build().also { INSTANCE = it }
            }
    }
}