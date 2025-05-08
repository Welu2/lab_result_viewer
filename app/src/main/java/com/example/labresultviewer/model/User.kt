package com.example.labresultviewer.model

import com.google.gson.annotations.SerializedName

// model/User.kt
data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("patientId") val patientId: String?,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String
    // Note: We exclude password since it's not needed client-side
)