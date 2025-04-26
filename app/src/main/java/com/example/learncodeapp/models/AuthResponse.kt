package com.example.learncodeapp.models

data class AuthResponse(
    val userId: Long,
    val username: String,
    val email: String,
    val message: String? = null // Поле message опционально, используется для ошибок
)