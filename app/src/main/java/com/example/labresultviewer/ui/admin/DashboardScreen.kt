package com.example.labresultviewer.ui.admin


import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.viewmodel.AdminDashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    stats?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Welcome back, Admin", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))
            Text("Appointments: ${it.totalAppointments}")
            Text("Total Patients: ${it.totalPatients}")
            Text("Total Lab Results: ${it.totalLabResults}")

            Spacer(Modifier.height(24.dp))
            Text("Upcoming Appointments:")
            it.upcomingAppointments.forEach { appt ->
                Text("${appt.time} - (${appt.testType})")
            }
        }
    } ?: run {
        CircularProgressIndicator()
    }
}



