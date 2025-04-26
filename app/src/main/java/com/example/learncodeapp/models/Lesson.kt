package com.example.learncodeapp.models

import com.google.gson.annotations.SerializedName

data class Lesson(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("language")
    val language: String
)