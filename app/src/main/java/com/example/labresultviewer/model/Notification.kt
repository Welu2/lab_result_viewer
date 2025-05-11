package com.example.labresultviewer.model

data class Notification(
    val id: Int,
    val message: String,
    val isRead: Boolean,
    val type: String,
    val recipientType: String,
    val createdAt: String,
    val user: UserLab? = null,
    val patientId: String? = null
) 