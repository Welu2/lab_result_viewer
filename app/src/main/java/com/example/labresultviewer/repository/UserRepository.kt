package com.example.labresultviewer.repository

import com.example.labresultviewer.model.TestResult
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.network.ApiService
import javax.inject.Inject
import retrofit2.Response

interface UserRepository {
    suspend fun getProfile(authHeader: String): UserProfile
    suspend fun getTestResults(patientId: String): List<TestResult>
    suspend fun deleteProfile(profileId: Int, authHeader: String): Response<Unit>
    suspend fun updateProfile(token: String, profileId: Int, profileData: Map<String, String>): Response<UserProfile>
    suspend fun updateEmail(token: String, email: String, password: String): Response<UserProfile>
}

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getProfile(authHeader: String): UserProfile {
        val response = apiService.getProfile(authHeader)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch profile")
        }
    }

    override suspend fun getTestResults(patientId: String): List<TestResult> {
        // Implement as needed, or throw if not used
        return emptyList()
    }

    override suspend fun deleteProfile(profileId: Int, authHeader: String): Response<Unit> {
        return apiService.deleteProfile(authHeader, profileId)
    }

    override suspend fun updateProfile(token: String, profileId: Int, profileData: Map<String, String>): Response<UserProfile> {
        return apiService.updateProfile(token, profileId, profileData)
    }

    override suspend fun updateEmail(token: String, email: String, password: String): Response<UserProfile> {
        return apiService.updateEmail(token, mapOf(
            "email" to email,
            "password" to password
        ))
    }
}
