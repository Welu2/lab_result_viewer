package com.example.labresultviewer.model

data class UserProfile(
    val id: Int,
    val name: String,
    val relative: String?,
    val dateOfBirth: String,
    val gender: String,
    val weight: Int,
    val height: Int,
    val bloodType: String,
    val phoneNumber: String?,
    val user: Users,
    val patientId: String
)
data class Users(
    val id: Int,
    val patientId: String,
    val email: String,
    val password: String,
    val role: String
)

