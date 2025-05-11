package com.example.labresultviewer.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.R
import com.example.labresultviewer.model.CreateProfileRequest
import com.example.labresultviewer.viewmodel.CreateProfileViewModel
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(onCreateClick: () -> Unit, onBackClick:()->Unit, createProfileViewModel: CreateProfileViewModel= hiltViewModel()) {
    var fullName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val dobDateState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var expanded by remember { mutableStateOf(false) }
    var selectedRelative by remember { mutableStateOf("") }
    var relativesExpanded by remember { mutableStateOf(false) }
    var relatives = listOf("Father", "Mother", "Brother", "Sister")
    var genderExpanded by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female")


    // Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> if (uri != null) imageUri = uri }
    )

    val headerHeight = 350.dp
    val cardOverlap = 200.dp
    val imageSize = 100.dp
    val imageOverlap = imageSize * 1f

    val createProfileAction = {
        val formattedDob = formatDateToIso(dob)
        val createProfileRequest = CreateProfileRequest(
            name = fullName,
            dateOfBirth = formattedDob,
            gender = gender.lowercase(),
            weight = weight.toDoubleOrNull(),
            height = height.toDoubleOrNull(),
            bloodType = bloodGroup,
            phoneNumber = "" // Add phone number input if needed
        )

        createProfileViewModel.createProfile(createProfileRequest)
    }

    // Observe ViewModel state
    val profileCreationState by createProfileViewModel.createProfileState.observeAsState()

    LaunchedEffect(profileCreationState) {
        if (profileCreationState?.isSuccess == true) {
            Log.d("CreateProfile", "Profile successfully created! Navigating to Home.") // ✅ Debug log
            Toast.makeText(context, "Profile Created!", Toast.LENGTH_SHORT).show()
            onCreateClick() // ✅ Navigate to Home
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top colored box
        Box(
            modifier = Modifier
                .height(headerHeight)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, end = 24.dp, top = 34.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Create A Health Profile",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // Floating card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .offset(y = headerHeight - cardOverlap)
                .zIndex(1f),
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(imageOverlap * 0.7f))

                // Form Fields
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("First name & Last name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Relatives Dropdown
                    ExposedDropdownMenuBox(
                        expanded = relativesExpanded,
                        onExpandedChange = { relativesExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedRelative.ifEmpty { "" },
                            onValueChange = { },
                            label = { Text("Relative") },
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = relativesExpanded)
                            },
                            shape = RoundedCornerShape(16.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = relativesExpanded,
                            onDismissRequest = { relativesExpanded = false }
                        ) {
                            relatives.forEach { relative ->
                                DropdownMenuItem(
                                    text = { Text(relative) },
                                    onClick = {
                                        selectedRelative = relative
                                        relativesExpanded = false
                                    }
                                )
                            }
                        }
                    }


                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = gender.ifEmpty { "" },
                            onValueChange = { },
                            label = { Text("Gender") },
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                            },
                            shape = RoundedCornerShape(16.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = dob,
                    onValueChange = { },
                    label = { Text("Date of Birth") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select Date",
                            modifier = Modifier.clickable { showDatePicker = true }
                        )
                    }
                )


                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (cm)") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = bloodGroup,
                        onValueChange = { },
                        label = { Text("Blood Group") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        bloodGroups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group) },
                                onClick = {
                                    bloodGroup = group
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {createProfileAction()},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Create Profile")
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
        Box(
            modifier = Modifier
                .size(imageSize)
                .align(Alignment.TopCenter) // Changed to TopCenter
                .offset(y = headerHeight - (imageSize)-150.dp)
                .zIndex(2f)
                .clip(CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") }
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            imageUri?.let { uri ->
                val bitmap = remember(uri) { loadBitmapFromUri(context, uri) }
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: CircularInitial(fullName)
            } ?: CircularInitial(fullName)

        }
        if (showDatePicker) {
            ShowDatePickerDialog(
                state = dobDateState,
                onDateSelected = { selectedDate ->
                    dob = selectedDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Fallback UI if no image is selected
@Composable
fun CircularInitial(userName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = userName.firstOrNull()?.uppercase() ?: "?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

fun formatDateToIso(input: String): String {
    // Try to parse "DD/MM/YYYY" or "MM/DD/YYYY" and output "YYYY-MM-DD"
    val possibleFormats = listOf("dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd")
    for (format in possibleFormats) {
        try {
            val inputFormat = SimpleDateFormat(format, Locale.US)
            val date = inputFormat.parse(input)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return outputFormat.format(date)
        } catch (_: Exception) {}
    }
    // If parsing fails, return as-is (backend will reject or store as 0000-00-00)
    return input
}