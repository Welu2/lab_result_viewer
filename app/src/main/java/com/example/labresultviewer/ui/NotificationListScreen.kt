package com.example.labresultviewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import com.example.labresultviewer.model.Notification
import com.example.labresultviewer.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    viewModel: NotificationViewModel,
    onBack: () -> Unit = {}
) {
    val notifications by viewModel.notifications.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState(initial = 0)

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (unreadCount > 0) {
                        TextButton(onClick = { viewModel.markAllAsRead() }) {
                            Text("Mark all as read")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(notifications) { notification ->
                NotificationItem(notification) {
                    viewModel.markAsRead(notification.id)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val background = if (!notification.isRead) Color(0xFFE3F2FD) else Color.White
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (notification.type) {
                "lab-result" -> Icons.Outlined.Notifications
                "appointment" -> Icons.Outlined.Event
                else -> Icons.Outlined.Notifications
            },
            contentDescription = null,
            tint = if (!notification.isRead) Color(0xFF388E3C) else Color.Gray
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = when (notification.type) {
                    "lab-result" -> "Lab Results Ready"
                    "appointment" -> "Appointment Reminder"
                    else -> "Notification"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(notification.message)
            Text(
                notification.createdAt.take(10), // Show only date part
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        if (!notification.isRead) {
            Box(
                Modifier
                    .size(10.dp)
                    .background(Color(0xFF388E3C), shape = CircleShape)
            )
        }
    }
} 