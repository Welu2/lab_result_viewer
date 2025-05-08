package com.example.labresultviewer.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.model.Appointment
import com.example.labresultviewer.viewmodel.AdminAppointmentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsApprovalScreen(
    viewModel: AdminAppointmentsViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val appointments by viewModel.pendingAppointments.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPendingAppointments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointments Approval", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("${appointments.size} appointments waiting for approval", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            if (loading) {
                CircularProgressIndicator()
            } else {
                appointments.forEach { appt ->
                    AppointmentApprovalCard(
                        appointment = appt,
                        onApprove = { viewModel.approve(appt.id) },
                        onDecline = { viewModel.decline(appt.id) }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AppointmentApprovalCard(
    appointment: Appointment,
    onApprove: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // You can use an avatar or icon here
                Text(
                    text = appointment.patient.patientId ?: "Unknown",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(appointment.status)
            }
            Spacer(Modifier.height(8.dp))
            Text("Date: ${appointment.date}")
            Text("Time: ${appointment.time}")
            Text("Test: ${appointment.testType}")
            Spacer(Modifier.height(8.dp))
            Row {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Approve")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = onDecline,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Decline")
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when (status) {
        "pending" -> Color(0xFFFFF59D)
        "confirmed" -> Color(0xFF81C784)
        "disapproved" -> Color(0xFFE57373)
        else -> Color.LightGray
    }
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = status.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}