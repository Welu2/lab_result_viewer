package com.example.labresultviewer.network

import com.example.labresultviewer.model.Notification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationService {
    @GET("/notifications/user")
    suspend fun getUserNotifications(
        @Header("Authorization") token: String
    ): List<Notification>

    @PATCH("/notifications/{id}/read")
    suspend fun markAsRead(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>
} 