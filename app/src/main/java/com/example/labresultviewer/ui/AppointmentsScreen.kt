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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.model.Appointment
import com.example.labresultviewer.viewmodel.PatientViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(viewModel: PatientViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val appointments = viewModel.appointments
    val bookingResult by viewModel.bookingResult

    // Sorting
    var sortBy by remember { mutableStateOf("date") }
    var showSortMenu by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        viewModel.loadUserAppointments()
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Title
            Text(
                "Appointments", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            // Sort/filter row + Book button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
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
                Button(
                    onClick = {
                        isReschedule = false; editIndex = -1
                        selectedTest = ""; selectedTime = ""
                        // Format initial date as YYYY-MM-DD
                        selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                        showScheduleDialog = true
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("Book New Appointment", color = Color.White, fontSize = 13.sp) }
            }
            Spacer(Modifier.height(8.dp))
            // Appointment list or empty
            Column(modifier = Modifier.padding(0.dp)) {
                Text("Upcoming Appointments", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                val displayList = if (sortBy == "name") appointments.sortedBy { it.testType } else appointments.sortedBy { it.date }
                if (displayList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No appointments found", color = Color.Gray)
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(0.dp)) {
                        itemsIndexed(displayList) { idx, appt ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                AppointmentItem(
                                    appointment = appt,
                                    onReschedule = {
                                        isReschedule = true; editIndex = idx
                                        selectedTest = appt.testType; selectedDate = appt.date; selectedTime = appt.time
                                        showScheduleDialog = true
                                    },
                                    onCancel = {
                                        pendingCancelIndex = idx; showCancelConfirmDialog = true
                                    },
                                    modifier = Modifier.padding(16.dp)
                                )
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
                        // Format date as YYYY-MM-DD
                        selectedDate = String.format("%04d-%02d-%02d", selYear, selMonth + 1, selDay)
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
                                if (isReschedule) {
                                    val apptId = appointments.getOrNull(editIndex)?.id
                                    if (apptId != null) {
                                        viewModel.updateAppointment(apptId, selectedTest, selectedDate, selectedTime) { success ->
                                            if (success) {
                                                showScheduleDialog = false
                                            } else {
                                                showErrorDialog = true
                                            }
                                        }
                                    } else {
                                        showErrorDialog = true
                                    }
                                } else {
                                    viewModel.bookAppointment(selectedTest, selectedDate, selectedTime)
                                }
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

        // Show booking result dialog
        if (bookingResult != null) {
            if (bookingResult!!.isSuccessful) {
                AlertDialog(
                    onDismissRequest = { viewModel.clearBookingResult() },
                    title = { Text("Successfully scheduled", fontWeight = FontWeight.Bold) },
                    confirmButton = {
                        Button(onClick = { viewModel.clearBookingResult() }, modifier = Modifier.fillMaxWidth()) {
                            Text("Go Home")
                        }
                    }
                )
            } else {
                AlertDialog(
                    onDismissRequest = { viewModel.clearBookingResult() },
                    title = { Text("Failed to schedule appointment", fontWeight = FontWeight.Bold) },
                    confirmButton = {
                        Button(onClick = { viewModel.clearBookingResult() }, modifier = Modifier.fillMaxWidth()) {
                            Text("Back to scheduling options")
                        }
                    }
                )
            }
        }

        // Cancel confirmation
        if (showCancelConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showCancelConfirmDialog = false },
                title = { Text("Are you sure you want to cancel this appointment?", fontWeight = FontWeight.Bold) },
                confirmButton = {
                    Button(
                        onClick = {
                            val apptId = appointments.getOrNull(pendingCancelIndex)?.id
                            if (apptId != null) {
                                viewModel.deleteAppointment(apptId) { success ->
                                    showCancelConfirmDialog = false
                                    showCancelSuccessDialog = success
                                }
                            } else {
                                showCancelConfirmDialog = false
                                showCancelSuccessDialog = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Yes, cancel appointment")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCancelConfirmDialog = false },
                        modifier = Modifier.fillMaxWidth()
                        ) {
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
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(appointment.testType, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            AssistChip(onClick = {}, label = { Text("Scheduled") }, shape = RoundedCornerShape(12.dp))
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF388E3C))
            Spacer(Modifier.width(6.dp))
            Text(appointment.date, fontSize = 14.sp)
            Spacer(Modifier.width(16.dp))
            Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF388E3C))
            Spacer(Modifier.width(6.dp))
            Text(appointment.time, fontSize = 14.sp)
        }
        Spacer(Modifier.height(12.dp))
        Row {
            Text("Reschedule", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable(onClick = onReschedule))
            Spacer(Modifier.width(24.dp))
            Text("Cancel", color = Color.Red, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable(onClick = onCancel))
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