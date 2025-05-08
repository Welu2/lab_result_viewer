package com.example.labresultviewer.repository

import com.example.labresultviewer.model.TestResult
import com.example.labresultviewer.model.UserProfile

interface UserRepository {
    suspend fun getProfile(authHeader: String): UserProfile
    suspend fun getTestResults(patientId: String): List<TestResult>
}
