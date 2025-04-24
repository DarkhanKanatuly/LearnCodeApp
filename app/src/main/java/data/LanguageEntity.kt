package com.example.learncodeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey val name: String,
    val iconResId: Int,
    val description: String,
    val videoId: String,
    val thumbnailUrl: String
)