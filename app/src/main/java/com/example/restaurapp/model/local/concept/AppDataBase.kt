// en app/src/main/java/com/example/restaurapp/data/local/AppDatabase.ktpackage com.example.restaurapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restaurapp.data.local.FamilyDao
import com.example.restaurapp.data.local.FamilyEntity
import com.example.restaurapp.data.local.TechnicalConceptEntity
import com.example.restaurapp.data.local.concept.ConceptDao

@Database(
    entities = [FamilyEntity::class, TechnicalConceptEntity::class],
    version = 1, // Si cambias el esquema, incrementa este número
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun familyDao(): FamilyDao
    abstract fun conceptDao(): ConceptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restaurapp_database"
                )
                    .fallbackToDestructiveMigration() // Útil en desarrollo, borra y recrea si la versión cambia
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
    