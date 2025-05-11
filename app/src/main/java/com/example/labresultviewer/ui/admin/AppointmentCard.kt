package com.example.labresultviewer.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.labresultviewer.model.Appointment

@Composable
fun AppointmentCard(appointment: Appointment) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Time Icon",
                tint = Color(0xFF5B8DEF),
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = appointment.time?.substring(0, 5) ?: "", // Format time to show only HH:mm
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = appointment.testType ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = appointment.patient?.patientId ?: "Unknown Patient",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
