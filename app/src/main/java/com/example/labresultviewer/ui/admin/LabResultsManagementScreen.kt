package com.example.labresultviewer.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.viewmodel.AdminLabResultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabResultsManagementScreen(
    viewModel: AdminLabResultViewModel = hiltViewModel(),
    onUploadReport: () -> Unit = {}
) {
    val labResults by viewModel.labResults.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var selectedLabResult by remember { mutableStateOf<LabResult?>(null) }
    var showModal by remember { mutableStateOf(false) }
    var modalType by remember { mutableStateOf("update") }

    LaunchedEffect(Unit) {
        viewModel.loadLabResults()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lab Results Management", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(onClick = onUploadReport) {
                        Text("Upload Report")
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
            // Search/filter UI here if needed

            if (loading) {
                CircularProgressIndicator()
            } else {
                labResults.forEach { result ->
                    LabResultRow(
                        labResult = result,
                        onAction = { type ->
                            selectedLabResult = result
                            modalType = type
                            showModal = true
                        }
                    )
                    Divider()
                }
            }
        }
    }

    if (showModal && selectedLabResult != null) {
        LabResultActionModal(
            labResult = selectedLabResult!!,
            type = modalType,
            onUpdate = { update ->
                viewModel.updateLabResult(selectedLabResult!!.id, update) { showModal = false }
            },
            onDelete = {
                viewModel.deleteLabResult(selectedLabResult!!.id) { showModal = false }
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun LabResultRow(
    labResult: LabResult,
    onAction: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = labResult.user?.profile?.name ?: "Patient ${labResult.patientId}",
                fontWeight = FontWeight.Bold
            )
            Text("ID: ${labResult.patientId ?: ""}", style = MaterialTheme.typography.bodySmall)
        }
        Column(Modifier.weight(1f)) {
            Text(labResult.title, fontWeight = FontWeight.Medium)
        }
        IconButton(onClick = { onAction("menu") }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Actions")
            DropdownMenu(
                expanded = false,
                onDismissRequest = {}
            ) {
                DropdownMenuItem(
                    text = { Text("Update") },
                    onClick = { onAction("update") }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = { onAction("delete") }
                )
            }
        }
    }
}

@Composable
fun LabResultActionModal(
    labResult: LabResult,
    type: String,
    onUpdate: (Map<String, String>) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(labResult.title) }
    var description by remember { mutableStateOf(labResult.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (type == "update") "Update Lab Result" else "Delete Lab Result") },
        text = {
            if (type == "update") {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )
                }
            } else {
                Text("Are you sure you want to delete this lab result?")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (type == "update") {
                        onUpdate(mapOf("title" to title, "description" to description))
                    } else {
                        onDelete()
                    }
                }
            ) {
                Text(if (type == "update") "Update" else "Delete")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}