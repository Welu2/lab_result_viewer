package com.example.labresultviewer.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user") val user: User?,
    @SerializedName("token") val token: Token,
    @SerializedName("role") val role: String? = null  // Some backends send role directly
)

data class Token(
    @SerializedName("access_token") val accessToken: String
)