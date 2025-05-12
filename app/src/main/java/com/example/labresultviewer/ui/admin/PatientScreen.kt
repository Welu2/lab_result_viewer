package com.example.labresultviewer.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.viewmodel.PatientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsScreen(
    viewModel: PatientViewModel = hiltViewModel(),
    navController: NavController
) {
    var showAddModal by remember { mutableStateOf(false) }
    var showEditModal by remember { mutableStateOf(false) }
    var showViewModal by remember { mutableStateOf(false) }
    var selectedPatient by remember { mutableStateOf<UserProfile?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Load patients when screen first appears
    LaunchedEffect(Unit) {
        viewModel.loadPatients()
    }

    val patients = viewModel.patients
    val isLoading by remember { viewModel.isLoading }
    val error by remember { viewModel.error }

    val filteredPatients = if (searchQuery.isBlank()) {
        patients
    } else {
        patients.filter { patient ->
            val query = searchQuery.lowercase()
            patient.name.lowercase().contains(query) ||
            patient.patientId.lowercase().contains(query) ||
            patient.user.email.lowercase().contains(query) ||
            patient.dateOfBirth.contains(query) ||
            patient.gender.lowercase().contains(query) ||
            patient.bloodType.lowercase().contains(query) ||
            (patient.phoneNumber?.lowercase()?.contains(query) ?: false) ||
            (patient.relative?.lowercase()?.contains(query) ?: false) ||
            patient.weight.toString().contains(query) ||
            patient.height.toString().contains(query)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patients", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showAddModal = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Patient")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }
            filteredPatients.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No patients found", color = Color.Gray)
                }
            }
            else -> {
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search patients…") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                        items(filteredPatients) { patient ->
                            PatientCard(
                                patient = patient,
                                onEdit = {
                                    selectedPatient = patient
                                    showEditModal = true
                                },
                                onViewProfile = {
                                    selectedPatient = patient
                                    showViewModal = true
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }

    // Add Patient Modal
    if (showAddModal) {
        AddPatientModal(
            onDismiss = { showAddModal = false },
            onAdd = { patientData ->
                viewModel.addPatient(patientData)
                showAddModal = false
            }
        )
    }

    // Edit Patient Modal
    if (showEditModal && selectedPatient != null) {
        EditPatientModal(
            patient = selectedPatient!!,
            onDismiss = { showEditModal = false },
            onEdit = { patientData ->
                viewModel.editPatient(selectedPatient!!.id.toString(), patientData)
                showEditModal = false
            }
        )
    }

    // View Patient Modal
    if (showViewModal && selectedPatient != null) {
        ViewPatientProfileModal(
            patient = selectedPatient!!,
            onDismiss = { showViewModal = false }
        )
    }
}

@Composable
fun PatientCard(
    patient: UserProfile,
    onEdit: () -> Unit,
    onViewProfile: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = patient.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Patient")
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ID: ${patient.patientId}", fontSize = 14.sp, color = Color.Gray)
            Text(text = "DOB: ${patient.dateOfBirth}", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = patient.user.email, fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            TextButton(onClick = onViewProfile, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("View Full Profile")
            }
        }
    }
}