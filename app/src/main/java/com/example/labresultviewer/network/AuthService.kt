package com.example.labresultviewer.network

import com.example.labresultviewer.model.AuthResponse
import com.example.labresultviewer.model.CreateProfileRequest
import com.example.labresultviewer.model.LoginRequest
import com.example.labresultviewer.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// network/AuthService.kt
interface AuthService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("/auth/signup")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("profile")
    suspend fun createProfile(
        @Body profile: CreateProfileRequest,
        @Header("Authorization") token: String
    ): Response<Unit> // or Response<Profile>, depending on your backend

}