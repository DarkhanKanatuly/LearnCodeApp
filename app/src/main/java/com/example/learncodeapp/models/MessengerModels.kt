package com.example.learncodeapp.models

data class Friend(
    val id: String,
    val name: String
)

data class Message(
    val sender: Friend,
    val content: String,
    val timestamp: Long
)