package com.example.learncodeapp.models

import com.example.learncodeapp.data.LessonEntity
import com.google.gson.annotations.SerializedName

data class Lesson(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("language")
    val language: String,
    val completed: Boolean = false
)

fun Lesson.toLessonEntity(): LessonEntity {
    return LessonEntity(
        id = id,
        title = title,
        content = content,
        language = language,
        completed = completed
    )
}

fun LessonEntity.toLesson(): Lesson {
    return Lesson(
        id = id,
        title = title,
        content = content,
        language = language,
        completed = completed
    )
}