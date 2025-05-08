package com.example.labresultviewer.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

// model/RegisterRequest.kt
data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("patientId") val patientId: String? = null // Optional if needed
)