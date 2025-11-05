package com.example.restaurapp.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restaurapp.model.local.concepts.ConceptDao
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.FamilyDao
import com.example.restaurapp.model.local.concepts.FamilyEntity
import com.example.restaurapp.model.local.user.UserDao
import com.example.restaurapp.model.local.user.UserEntity

@Database(entities = [UserEntity::class, ConceptEntity::class, FamilyEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun conceptDao(): ConceptDao

    abstract fun familyDao(): FamilyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}
