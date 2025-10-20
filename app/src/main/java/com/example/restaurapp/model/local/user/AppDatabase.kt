package com.example.restaurapp.model.local.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Fixed the missing ::class on the last entity
@Database(
    entities = [
        UserEntity::class,
        FamilyEntity::class,
        FormativeConceptEntity::class,
        TechnicalConceptEntity::class // <<< FIX: Added ::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun familyDao(): FamilyDao
    abstract fun conceptDao(): ConceptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Renamed to getDatabase for clarity, as discussed previously
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // 2. Added a migration strategy to handle future updates.
                    // This is essential for development.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
