package com.example.learncodeapp.models

import com.example.learncodeapp.screens.Friend

data class Chat(
    val id: Int,
    val friend: Friend, // Используем Friend из ProfileScreen
    val lastMessage: String,
    val timestamp: Long
)

data class Message(
    val id: Int,
    val chatId: Int,
    val senderId: Int, // ID отправителя (например, 0 для текущего пользователя, ID друга для других)
    val content: String,
    val timestamp: Long
)