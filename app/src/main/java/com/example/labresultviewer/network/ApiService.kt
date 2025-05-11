package com.example.labresultviewer.network

import com.example.labresultviewer.model.DashboardStats
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.model.TestResult
import com.example.labresultviewer.model.RegisterRequest
import com.example.labresultviewer.model.AuthResponse
import com.example.labresultviewer.model.Appointment
import retrofit2.Response
import retrofit2.http.*

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

    @DELETE("/profile/{id}")
    suspend fun deleteProfile(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @POST("/auth/signup")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("/profile")
    suspend fun createProfile(
        @Header("Authorization") token: String,
        @Body profileData: Map<String, String>
    ): Response<UserProfile>

    @PATCH("/profile/{id}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body profileData: Map<String, String>
    ): Response<UserProfile>

    @POST("/appointments")
    suspend fun bookAppointment(
        @Header("Authorization") token: String,
        @Body appointment: Map<String, String>
    ): Response<Appointment>

    @GET("/appointments/me")
    suspend fun getUserAppointments(
        @Header("Authorization") token: String
    ): List<Appointment>

    @DELETE("/appointments/{id}")
    suspend fun deleteAppointment(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>
}
