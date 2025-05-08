package com.example.labresultviewer.repository

import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.network.ApiService

class PatientRepository(private val apiService: ApiService) {
    suspend fun getAllPatients(token: String): Result<List<UserProfile>> {
        return try {
            val response = apiService.getAllPatients(token)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch patients: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Add other repository methods as needed
}
