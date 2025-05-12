package com.example.labresultviewer.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.ui.admin.EditPatientModal
import com.example.labresultviewer.viewmodel.UserProfileViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userName: String,
    dob: String,
    onChangeEmail: () -> Unit,
    onNotificationSetting: () -> Unit,
    onLogout: () -> Unit,
    onDeleteProfile: () -> Boolean,
    profileImageUrl: String? = null,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEmailChangeModal by remember { mutableStateOf(false) }
    var showEditProfileModal by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val emailChangeState by viewModel.emailChangeState.collectAsState()

    // Handle email change result
    LaunchedEffect(emailChangeState) {
        emailChangeState.getOrNull()?.let { success ->
            if (success) {
                Toast.makeText(context, "Email changed successfully", Toast.LENGTH_SHORT).show()
                showEmailChangeModal = false
            }
        }
        emailChangeState.exceptionOrNull()?.let { error ->
            Toast.makeText(context, "Failed to change email: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(32.dp))
        Text("Profile", fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Spacer(Modifier.height(24.dp))

        // Profile Card
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
                .clickable { showEditProfileModal = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                if (profileImageUrl == null) {
                    // Show initial
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE57373)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.firstOrNull()?.uppercase() ?: "?",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                } else {
                    // TODO: Load image with Coil/Glide if you have a URL
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(userName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(dob, color = Color.Gray, fontSize = 15.sp)
                }
                Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.LightGray)
            }
        }

        Spacer(Modifier.height(32.dp))

        // Action List
        ProfileActionItem(
            icon = Icons.Default.Email,
            text = "Change Email",
            onClick = { showEmailChangeModal = true }
        )
        ProfileActionItem(
            icon = Icons.Default.Notifications,
            text = "Notification Setting",
            onClick = onNotificationSetting
        )
        ProfileActionItem(
            icon = Icons.Default.Logout,
            text = "Log out",
            onClick = onLogout,
            color = Color(0xFF388E3C)
        )
        Divider(Modifier.padding(vertical = 8.dp))
        Text(
            "Delete Profile",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDeleteDialog = true }
                .padding(vertical = 12.dp)
        )
        Divider()
    }

    // Email Change Modal
    if (showEmailChangeModal) {
        EmailChangeModal(
            currentEmail = viewModel.profile.value?.user?.email ?: "",
            onDismiss = { showEmailChangeModal = false },
            onEmailChange = { newEmail, password ->
                viewModel.changeEmail(newEmail, password)
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val deleted = onDeleteProfile()
                                if (deleted) {
                                    showDeleteDialog = false
                                    // Navigate to Welcome page (handled in parent)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Delete Account", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { showDeleteDialog = false },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            },
            dismissButton = { },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0x1AFF0000), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            title = null,
            text = {
                Text(
                    "Are you sure you want to delete your account?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    if (showEditProfileModal && viewModel.profile.value != null) {
        EditPatientModal(
            patient = viewModel.profile.value!!,
            onDismiss = { showEditProfileModal = false },
            onEdit = { updatedFields ->
                viewModel.updateProfile(updatedFields)
                showEditProfileModal = false
            }
        )
    }
}

@Composable
fun ProfileActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    color: Color = Color(0xFF1976D2)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Spacer(Modifier.width(16.dp))
        Text(text, color = color, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.LightGray)
    }
}