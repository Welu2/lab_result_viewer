package com.example.labresultviewer.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsScreen(onLogout: () -> Unit, onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(8.dp))
            Text("Settings", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Manage your account and app preferences",
            color = Color(0xFF8E99A8),
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text("Account", color = Color(0xFF8E99A8), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Spacer(Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(vertical = 8.dp)) {
                AdminSettingsItem(
                    icon = Icons.Default.Person,
                    text = "Profile Information",
                    onClick = {},
                )
                Divider()
                AdminSettingsItem(
                    icon = Icons.Default.Notifications,
                    text = "Notifications",
                    onClick = {},
                )
                Divider()
                AdminSettingsItem(
                    icon = Icons.Default.Security,
                    text = "Security",
                    onClick = {},
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x1AFF0000)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLogout() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 16.dp)
                    .padding(horizontal = 90.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFFF3B30))
                Spacer(Modifier.width(8.dp))
                Text("Log Out", color = Color(0xFFFF3B30), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun AdminSettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFF0F1F3), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF8E99A8))
        }
        Spacer(Modifier.width(16.dp))
        Text(text, color = Color.Black, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color(0xFF8E99A8))
    }
} 