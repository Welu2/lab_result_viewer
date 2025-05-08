package com.example.labresultviewer.network

import com.example.labresultviewer.model.Appointment
import retrofit2.Response
import retrofit2.http.*

interface AdminAppointmentService {
    @GET("/appointments")
    suspend fun getAllAppointments(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null
    ): List<Appointment>

    @PATCH("/appointments/{id}/status")
    suspend fun updateAppointmentStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body status: Map<String, String>
    ): Response<Appointment>
}