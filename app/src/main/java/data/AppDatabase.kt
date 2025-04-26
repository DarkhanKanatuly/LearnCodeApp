package com.example.learncodeapp.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val language: String,
    val completed: Boolean = false
)

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons")
    suspend fun getAllLessons(): List<LessonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lessons: List<LessonEntity>)

    @Query("DELETE FROM lessons")
    suspend fun deleteAll()

    @Query("UPDATE lessons SET completed = :completed WHERE id = :lessonId")
    suspend fun updateLessonCompletion(lessonId: Long, completed: Boolean)
}

@Database(entities = [LessonEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE lessons ADD COLUMN completed INTEGER NOT NULL DEFAULT 0")
    }
}