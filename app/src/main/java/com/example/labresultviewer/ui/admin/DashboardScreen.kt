package com.example.labresultviewer.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.viewmodel.AdminDashboardViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    stats?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Dashboard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { navController.navigate(Screen.AppointmentsApproval.route) }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF3B5BDB),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Text("Welcome back, Admin", color = Color.Gray, fontSize = 16.sp)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    icon = Icons.Default.CalendarToday,
                    label = "Appointments",
                    value = it.totalAppointments.toString(),
                    badge = "Today"
                )
                StatCard(
                    icon = Icons.Default.Group,
                    label = "Total Patients",
                    value = it.totalPatients.toString()
                )
            }
            Spacer(Modifier.height(12.dp))
            StatCard(
                icon = Icons.Default.CheckCircle,
                label = "Total Lab Results",
                value = it.totalLabResults.toString(),
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Spacer(Modifier.height(24.dp))
            Text("Upcoming Appointments", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp)) {
                    it.upcomingAppointments.forEach { appt ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(Color(0xFFE3EAFE), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(appt.time, color = Color(0xFF3B5BDB), fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(appt.patient?.patientId ?: appt.patientName ?: "Unknown", fontWeight = FontWeight.Bold)
                                Text(appt.testType, color = Color.Gray, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { /* TODO: Navigate to all appointments */ }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("View All Appointments", color = Color(0xFF3B5BDB))
                    }
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    badge: String? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        // modifier = modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF3B5BDB), modifier = Modifier.size(24.dp))
                if (badge != null) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE3EAFE), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(badge, color = Color(0xFF3B5BDB), fontSize = 12.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(label, color = Color.Gray, fontSize = 14.sp)
        }
    }
}



