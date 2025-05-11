package com.example.labresultviewer.repository

import com.example.labresultviewer.model.Notification
import com.example.labresultviewer.network.NotificationService
import retrofit2.Response
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val service: NotificationService) {
    suspend fun getUserNotifications(token: String): List<Notification> {
        return service.getUserNotifications(token)
    }

    suspend fun markAsRead(token: String, id: Int): Response<Unit> {
        return service.markAsRead(token, id)
    }
} 