package com.example.labresultviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.labresultviewer.data.SessionManager

@Composable
fun SuccessScreen(
    onCompleteClick: () -> Unit,
    sessionManager: SessionManager,
    navController: NavController
) {
    val userRole by sessionManager.userRole.collectAsState(initial = "user")

    LaunchedEffect(Unit) {
        // Navigate based on user role
        when (userRole?.lowercase()) {
            "admin" -> {
                navController.navigate(Screen.AdminDashboard.route) {
                    popUpTo(Screen.Success.route) { inclusive = true }
                }
            }
            else -> {
                onCompleteClick()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Account Created Successfully!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Redirecting you to the appropriate screen...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
