package com.example.labresultviewer.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

// Appointment data model
data class Appointment(
    val testName: String,
    val date: String,
    val time: String,
    val notes: String = ""
)

// No initial appointments
private val sampleAppointments = emptyList<Appointment>()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen() {
    val context = LocalContext.current

    // Sorting
    var sortBy by remember { mutableStateOf("date") }
    var showSortMenu by remember { mutableStateOf(false) }

    // List of appointments
    val appointments = remember { mutableStateListOf<Appointment>().apply { addAll(sampleAppointments) } }

    // Booking/rescheduling dialog state
    var showScheduleDialog by remember { mutableStateOf(false) }
    var isReschedule by remember { mutableStateOf(false) }
    var editIndex by remember { mutableStateOf(-1) }

    // Form state
    var selectedTest by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Feedback dialogs
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    // Cancellation flow
    var pendingCancelIndex by remember { mutableStateOf(-1) }
    var showCancelConfirmDialog by remember { mutableStateOf(false) }
    var showCancelSuccessDialog by remember { mutableStateOf(false) }

    // Dropdown options
    val testTypes = listOf("Ultrasound", "CT Scan", "MRI", "Blood Work", "CBC", "Urinal Analysis", "X-Ray")
    val timeSlots = listOf("09:00 AM","09:30 AM","10:00 AM","10:30 AM","11:00 AM","11:30 AM")

    // Date utilities
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Title and sort
            Text(
                "Appointments", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort")
                }
                DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                    DropdownMenuItem(text = { Text("By Name") }, onClick = { sortBy = "name"; showSortMenu = false })
                    DropdownMenuItem(text = { Text("By Date") }, onClick = { sortBy = "date"; showSortMenu = false })
                }
                Spacer(Modifier.width(8.dp))
                Text("by $sortBy", fontSize = 16.sp)
            }

            Spacer(Modifier.height(16.dp))

            // Header + Book button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("List of Appointments", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Button(
                    onClick = {
                        isReschedule = false; editIndex = -1;
                        selectedTest = ""; selectedTime = ""; selectedDate = "$day/${month+1}/$year";
                        showScheduleDialog = true
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
                ) { Text("Book New Appointment", color = Color.White) }
            }
            Spacer(Modifier.height(8.dp))

            // Appointment list or empty
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Upcoming Appointments", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    val displayList = if (sortBy == "name") appointments.sortedBy { it.testName } else appointments.sortedBy { it.date }
                    if (displayList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No appointments found", color = Color.Gray)
                        }
                    } else {
                        LazyColumn {
                            itemsIndexed(displayList) { idx, appt ->
                                AppointmentItem(
                                    appointment = appt,
                                    onReschedule = {
                                        isReschedule = true; editIndex = idx;
                                        selectedTest = appt.testName; selectedDate = appt.date; selectedTime = appt.time;
                                        showScheduleDialog = true
                                    },
                                    onCancel = {
                                        pendingCancelIndex = idx; showCancelConfirmDialog = true
                                    }
                                )
                                if (idx < displayList.lastIndex) Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        // Show DatePicker when requested
        if (showDatePicker) {
            LaunchedEffect(showDatePicker) {
                DatePickerDialog(
                    context,
                    { _: DatePicker, selYear, selMonth, selDay ->
                        selectedDate = "$selDay/${selMonth + 1}/$selYear"
                        showDatePicker = false
                    },
                    year, month, day
                ).show()
            }
        }

        // Schedule/Reschedule dialog
        if (showScheduleDialog) {
            AlertDialog(
                onDismissRequest = { showScheduleDialog = false },
                title = { Text(if (isReschedule) "Reschedule Appointment" else "Book New Appointment", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        if (!isReschedule) {
                            DropdownMenuField("Test Type", testTypes, selectedTest) { selectedTest = it }
                            Spacer(Modifier.height(8.dp))
                        }
                        Text("Preferred Date", fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(selectedDate)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Available Time Slots", fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(4.dp))
                        timeSlots.chunked(2).forEach { rowSlots ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowSlots.forEach { slot ->
                                    val isSelected = slot == selectedTime
                                    OutlinedButton(
                                        onClick = { selectedTime = slot },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Text(slot)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showScheduleDialog = false
                            if (selectedDate.isNotBlank() && selectedTime.isNotBlank() && (isReschedule || selectedTest.isNotBlank())) {
                                val appt = Appointment(
                                    testName = if (isReschedule) appointments[editIndex].testName else selectedTest,
                                    date = selectedDate,
                                    time = selectedTime
                                )
                                if (isReschedule && editIndex >= 0) appointments[editIndex] = appt else appointments.add(0, appt)
                                showSuccessDialog = true
                            } else {
                                showErrorDialog = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
                    ) {
                        Text("Schedule Appointment", color = Color.White)
                    }
                }
            )
        }

        // Success dialog
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Successfully scheduled", fontWeight = FontWeight.Bold) },
                confirmButton = {
                    Button(onClick = { showSuccessDialog = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("Go Home")
                    }
                }
            )
        }

        // Error dialog
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Scheduling failed", fontWeight = FontWeight.Bold) },
                confirmButton = {
                    Button(
                        onClick = { showErrorDialog = false; showScheduleDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to scheduling options")
                    }
                }
            )
        }

        // Cancel confirmation
        if (showCancelConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showCancelConfirmDialog = false },
                title = { Text("Are you sure you want to cancel this appointment?", fontWeight = FontWeight.Bold) },
                confirmButton = {
                    Button(
                        onClick = {
                            appointments.removeAt(pendingCancelIndex)
                            showCancelConfirmDialog = false
                            showCancelSuccessDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Yes, cancel appointment")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCancelConfirmDialog = false }) {
                        Text("No, keep")
                    }
                }
            )
        }

        // Cancel success
        if (showCancelSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showCancelSuccessDialog = false },
                title = { Text("Successfully cancelled", fontWeight = FontWeight.Bold) },
                confirmButton = {
                    Button(onClick = { showCancelSuccessDialog = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("Go Home")
                    }
                }
            )
        }
    }
}

@Composable
fun AppointmentItem(
    appointment: Appointment,
    onReschedule: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(appointment.testName, fontWeight = FontWeight.SemiBold)
            AssistChip(onClick = {}, label = { Text("Scheduled") }, shape = RoundedCornerShape(12.dp))
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(appointment.date, fontSize = 12.sp)
        }
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(appointment.time, fontSize = 12.sp)
        }
        if (appointment.notes.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(appointment.notes, fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Text("Reschedule", color = Color(0xFF2ECC71), modifier = Modifier.clickable(onClick = onReschedule))
            Spacer(Modifier.width(16.dp))
            Text("Cancel", color = Color.Red, modifier = Modifier.clickable(onClick = onCancel))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (selected.isEmpty()) label else selected,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = {
                    onSelectedChange(opt)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppointmentsScreen() {
    MaterialTheme { AppointmentsScreen() }
}
