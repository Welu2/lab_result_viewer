package com.example.labresultviewer.network

import com.example.labresultviewer.model.DashboardStats
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.model.TestResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

// Define your network API interface
interface ApiService {

    // Endpoint to get the profile using an Authorization header (token)
    @GET("/profile/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserProfile>


        @GET("admin/dashboard")
        suspend fun getDashboardStats(): DashboardStats

    @GET("/profile")
    suspend fun getAllPatients(
        @Header("Authorization") token: String
    ): Response<List<UserProfile>>

    @GET("/profile/by-patient/{patientId}")
    suspend fun getPatientById(
        @Header("Authorization") token: String,
        @Path("patientId") patientId: String
    ): Response<UserProfile>
}
