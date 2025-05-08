package com.example.labresultviewer.model

data class CreateProfileRequest(
    val name: String,
    val dateOfBirth: String,
    val gender: String,
    val weight: Double?,
    val height: Double?,
    val bloodType: String?,
    val phoneNumber: String?
)
