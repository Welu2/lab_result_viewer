package com.example.labresultviewer.repository

import com.example.labresultviewer.model.Appointment
import com.example.labresultviewer.network.AdminAppointmentService
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AdminAppointmentsRepository @Inject constructor(
    private val service: AdminAppointmentService
) {
    suspend fun getPendingAppointments(token: String): List<Appointment> {
        return service.getAllAppointments(token, "pending")
    }

    suspend fun getAllAppointments(token: String): List<Appointment> {
        return service.getAllAppointments(token)
    }

    suspend fun updateAppointmentStatus(token: String, id: Int, status: String): Response<Unit> {
        return service.updateAppointmentStatus(token, id, mapOf("status" to status))
    }

    fun filterAppointments(appointments: List<Appointment>): Triple<List<Appointment>, List<Appointment>, List<Appointment>> {
    val now = LocalDateTime.now()
    val today = now.toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val todayList = mutableListOf<Appointment>()
    val upcomingList = mutableListOf<Appointment>()
    val pastList = mutableListOf<Appointment>()

    for (appointment in appointments) {
        try {
            val appointmentDateTime = LocalDateTime.parse("${appointment.date} ${appointment.time}", formatter)
            when {
                appointmentDateTime.toLocalDate() == today && appointmentDateTime.isAfter(now) -> todayList.add(appointment)
                appointmentDateTime.toLocalDate().isAfter(today) -> upcomingList.add(appointment)
                else -> pastList.add(appointment)
            }
        } catch (e: Exception) {
            pastList.add(appointment)
        }
    }
    return Triple(todayList, upcomingList, pastList)
}
}