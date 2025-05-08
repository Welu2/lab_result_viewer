package com.example.labresultviewer.repository

import com.example.labresultviewer.model.Appointment
import com.example.labresultviewer.network.AdminAppointmentService
import retrofit2.Response
import javax.inject.Inject

class AdminAppointmentsRepository @Inject constructor(
    private val service: AdminAppointmentService
) {
    suspend fun getPendingAppointments(token: String): List<Appointment> {
        return service.getAllAppointments(token, "pending")
    }

    suspend fun updateAppointmentStatus(token: String, id: Int, status: String): Response<Appointment> {
        return service.updateAppointmentStatus(token, id, mapOf("status" to status))
    }
}