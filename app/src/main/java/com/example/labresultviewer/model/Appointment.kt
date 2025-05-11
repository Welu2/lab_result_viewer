package com.example.labresultviewer.model

data class Appointment(
    val id: Int,
    val testType: String,
    val date: String,  // Use String if date comes in ISO format from backend
    val time: String,
    val patientName: String,
    val status: String,
    val patient: User // Assuming there's a User model
)
