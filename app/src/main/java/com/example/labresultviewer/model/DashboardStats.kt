package com.example.labresultviewer.model

data class DashboardStats(
    val totalAppointments: Int,
    val totalPatients: Int,
    val totalLabResults: Int,
    val upcomingAppointments: List<Appointment>
)
