package com.example.labresultviewer.ui.admin

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.viewmodel.labresults.LabResultsViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload", null, context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        tempFile
    } catch (e: Exception) {
        null
    }
}

fun getFileName(context: Context, uri: Uri): String {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        it.getString(nameIndex)
    } ?: "Unknown file"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadLabReportScreen(
    viewModel: LabResultsViewModel,
    onBack: () -> Unit = {},
) {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var reportDate by remember { mutableStateOf("") }
    var patientId by remember { mutableStateOf("") }
    var reportType by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val reportTypes = listOf("Blood Test", "Urine Test", "X-Ray", "Other")
    val uploadResult by viewModel.uploadResult.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    LaunchedEffect(uploadResult) {
        uploadResult?.let { result ->
            if (result.isSuccessful) {
                snackbarHostState.showSnackbar("Report uploaded successfully!")
                // Reset form
                selectedFileUri = null
                reportDate = ""
                patientId = ""
                reportType = ""
            } else {
                snackbarHostState.showSnackbar("Failed to upload report: ${result.message()}")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Lab Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { 
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 80.dp)
            ) 
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .clickable { filePickerLauncher.launch("application/pdf") },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (selectedFileUri != null) {
                        // Show PDF icon and filename when file is selected
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Selected: ${getFileName(context, selectedFileUri!!)}",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        // Show upload icon when no file is selected
                        Icon(
                            Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = Color(0xFFB0B0B0),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tap to upload your report", color = Color(0xFFB0B0B0))
                        Text("PDF, JPG or PNG", color = Color(0xFFB0B0B0), fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = reportDate,
                onValueChange = { reportDate = it },
                label = { Text("Report Date") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        // Show date picker dialog
                        val calendar = Calendar.getInstance()
                        val datePicker = android.app.DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                                calendar.set(year, month, dayOfMonth)
                                reportDate = sdf.format(calendar.time)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePicker.show()
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = patientId,
                onValueChange = { patientId = it },
                label = { Text("Patient ID") },
                leadingIcon = { Icon(Icons.Default.CloudUpload, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = reportType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Report Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    reportTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                reportType = type
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    // Handle file upload
                    if (selectedFileUri != null && patientId.isNotBlank() && reportType.isNotBlank()) {
                        val file = getFileFromUri(context, selectedFileUri!!)
                        if (file != null) {
                            val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
                            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                            viewModel.uploadLabReport(patientId, body, reportType)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && selectedFileUri != null && patientId.isNotBlank() && reportType.isNotBlank()
            ) {
                Text("Upload Report")
            }
            if (uploadResult != null) {
                if (uploadResult!!.isSuccessful) {
                    LaunchedEffect(Unit) {
                        viewModel.sendLabResultToUser(patientId)
                        snackbarHostState.showSnackbar("Report uploaded and notification sent successfully!")
                    }
                } else {
                    Text("Upload failed!", color = Color.Red, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
} 