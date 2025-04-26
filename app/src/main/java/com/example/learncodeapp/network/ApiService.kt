package com.example.learncodeapp.network

import com.example.learncodeapp.models.AuthRequest
import com.example.learncodeapp.models.AuthResponse
import com.example.learncodeapp.models.Lesson
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/lessons")
    suspend fun getLessons(): Response<List<Lesson>>

    @POST("api/auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>
}