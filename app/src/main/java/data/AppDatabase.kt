package com.example.learncodeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LanguageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
}