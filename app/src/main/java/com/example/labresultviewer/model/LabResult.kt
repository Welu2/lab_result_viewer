package com.example.labresultviewer.model

data class LabResult(
    val id: Int,
    val title: String,
    val description: String? = null,
    val filePath: String? = null,
    val userId: Int? = null,
    val reportDate: String? = null,
    val reportType: String? = null,
    val status: String? = null,
    val downloadUrl: String? = null,
    val user: UserLab? = null,
    val patientId: String? = null,
    val isSent: Boolean = false,
    val createdAt: String? = null
)

data class UserLab(
    val id: Int,
    val patientId: String,
    val email: String,
    val password: String,
    val role: String,
    val profile: Profile?
)

data class Profile(
    val id: Int,
    val name: String,
    val relative: String,
    val dateOfBirth: String,
    val gender: String,
    val weight: Int,
    val height: Int,
    val bloodType: String,
    val phoneNumber: String,
    val user: User?,
    val patientId: String
)
