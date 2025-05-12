package com.example.labresultviewer.repository

import com.example.labresultviewer.model.Appointment
import com.example.labresultviewer.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class AppointmentRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun bookAppointment(token: String, testType: String, date: String, time: String): Response<Appointment> {
        val body = mapOf(
            "testType" to testType,
            "date" to date,
            "time" to time
        )
        return apiService.bookAppointment(token, body)
    }

    suspend fun getUserAppointments(token: String): List<Appointment> {
        return apiService.getUserAppointments(token)
    }

    suspend fun deleteAppointment(token: String, id: Int): Response<Unit> {
        return apiService.deleteAppointment(token, id)
    }

    suspend fun updateAppointment(token: String, id: Int, testType: String, date: String, time: String): Response<Appointment> {
        val body = mapOf(
            "testType" to testType,
            "date" to date,
            "time" to time
        )
        return apiService.updateAppointment(token, id, body)
    }
} 