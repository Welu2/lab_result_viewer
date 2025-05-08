package com.example.labresultviewer.data


data class Appointment(
    val time: String,
    val testType: String,
    val patientName: String
)

fun getDummyAppointmentsToday(): List<Appointment> = listOf(
    Appointment("09:00 AM", "CT Scan", "Mahlet Andualem"),
    Appointment("10:30 AM", "MRI Scan", "Biruk Asmamaw"),
    Appointment("11:45 AM", "MRI Scan", "Fikr Girma"),
    Appointment("01:15 PM", "Dr. Williams", "Yonatan Ayalew")
)

fun getDummyAppointmentsUpcoming(): List<Appointment> = listOf(
    Appointment("May 18 - 09:00 AM", "X-Ray", "Hana Tilahun"),
    Appointment("May 19 - 11:00 AM", "CT Scan", "Lidya Mekonnen")
)

fun getDummyAppointmentsPast(): List<Appointment> = listOf(
    Appointment("May 10 - 10:30 AM", "MRI Scan", "Kidus Fikru"),
    Appointment("May 12 - 01:45 PM", "Ultrasound", "Saron Abebe")
)
