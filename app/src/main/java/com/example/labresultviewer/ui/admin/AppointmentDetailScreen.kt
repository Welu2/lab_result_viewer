package com.example.labresultviewer.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.labresultviewer.data.getDummyAppointmentsPast
import com.example.labresultviewer.data.getDummyAppointmentsToday
import com.example.labresultviewer.data.getDummyAppointmentsUpcoming

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen() {
    val tabs = listOf("Today", "Upcoming", "Past")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val appointments = when (selectedTabIndex) {
        0 -> getDummyAppointmentsToday()
        1 -> getDummyAppointmentsUpcoming()
        else -> getDummyAppointmentsPast()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Appointments", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { /* TODO: Handle back */ }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        FilterRow()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(appointments) { appointment ->
                AppointmentCard(appointment)
            }
        }
    }
}

@Composable
fun FilterRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Calendar",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("May 17, 2023", color = Color.Gray)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Filter", color = Color.Gray)
        }
    }
}

@Preview
@Composable
fun AppointmentScreenPreview() {
    AppointmentScreen()
}