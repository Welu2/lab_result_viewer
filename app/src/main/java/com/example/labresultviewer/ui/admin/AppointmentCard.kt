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
import com.example.labresultviewer.data.Appointment

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
                Text(appointment.time, style = MaterialTheme.typography.titleMedium)
                Text(appointment.testType, style = MaterialTheme.typography.bodyMedium)
                Text(appointment.patientName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
