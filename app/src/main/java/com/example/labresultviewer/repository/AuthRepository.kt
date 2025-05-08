package com.example.labresultviewer.repository

import android.util.Log
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.LoginRequest
import com.example.labresultviewer.model.RegisterRequest
import com.example.labresultviewer.model.User
import com.example.labresultviewer.network.AuthService

import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: AuthService,
    private val sessionManager: SessionManager // ✅ Inject SessionManager
) {
    suspend fun login(email: String, password: String): Result<UserWithToken> {
        return try {
            val response = service.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    val token = authResponse.token.accessToken
                    sessionManager.saveToken(token)
                    Result.success(UserWithToken(token = token))
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        patientId: String? = null
    ): Result<UserWithToken> {
        return try {
            val response = service.register(RegisterRequest(email, password, patientId))
            Log.d("AuthRepository", "Register response: ${response.code()} ${response.errorBody()?.string()}")
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    val token = authResponse.token.accessToken
                    sessionManager.saveToken(token)
                    Result.success(UserWithToken(token = token))
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

// Additional model for convenience
data class UserWithToken(
    val token: String
)