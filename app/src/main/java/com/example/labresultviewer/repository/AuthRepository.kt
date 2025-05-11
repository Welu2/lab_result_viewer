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
            Log.d("AuthRepository", "Login response code: ${response.code()}")
            Log.d("AuthRepository", "Login response body: ${response.body()}")
            Log.d("AuthRepository", "Login error body: ${response.errorBody()?.string()}")
            
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    Log.d("AuthRepository", "Auth response: $authResponse")
                    val token = authResponse.token.accessToken
                    
                    // Extract role from JWT token
                    val role = try {
                        val parts = token.split(".")
                        if (parts.size == 3) {
                            val payload = parts[1]
                            val decodedPayload = String(android.util.Base64.decode(payload, android.util.Base64.DEFAULT))
                            val jsonObject = org.json.JSONObject(decodedPayload)
                            jsonObject.getString("role")
                        } else {
                            // Fallback to email check if JWT parsing fails
                            if (email.contains("@pulse.org")) "admin" else "user"
                        }
                    } catch (e: Exception) {
                        Log.e("AuthRepository", "Error parsing JWT token", e)
                        // Fallback to email check
                        if (email.contains("@pulse.org")) "admin" else "user"
                    }
                    
                    Log.d("AuthRepository", "User role: $role")
                    
                    sessionManager.saveToken(token)
                    sessionManager.saveUserRole(role)
                    Result.success(UserWithToken(token = token, role = role))
                } ?: run {
                    Log.e("AuthRepository", "Response body is null")
                    Result.failure(Exception("Empty response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Login failed: ${response.code()} - $errorBody")
                Result.failure(Exception("Login failed: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login error", e)
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
                    val user = authResponse.user
                    if (user != null) {
                        val role = user.role
                        sessionManager.saveToken(token)
                        sessionManager.saveUserRole(role)
                        Result.success(UserWithToken(token = token, role = role))
                    } else {
                        Result.failure(Exception("User data is missing from response"))
                    }
                } ?: Result.failure(Exception("Empty response"))
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Registration failed: ${response.code()} - $errorBody")
                Result.failure(Exception("Registration failed: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Registration error", e)
            Result.failure(e)
        }
    }

}

// Additional model for convenience
data class UserWithToken(
    val token: String,
    val role: String
)