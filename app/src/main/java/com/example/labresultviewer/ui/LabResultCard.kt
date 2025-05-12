package com.example.labresultviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import com.example.labresultviewer.model.LabResult

@Composable
fun LabResultCard(
    result: LabResult,
    onView: () -> Unit = {},
    onDownload: () -> Unit = {},
    onCopyLink: () -> Unit = {},
    onDownloadPdf: () -> Unit = {}
) {
    var showShareDialog by remember { mutableStateOf(false) }
    
    val (statusText, statusColor) = when (result.status?.lowercase()) {
        "normal", "normal results" -> "Normal Results" to Color(0xFF3CB371)
        "requires attention" -> "Requires Attention" to Color(0xFFFFA500)
        "follow-up needed" -> "Follow-Up Needed" to Color(0xFFDB3B3B)
        else -> (result.status ?: "") to Color.Gray
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = result.title ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (!result.reportDate.isNullOrBlank()) {
                        Text(
                            text = result.reportDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    if (!result.reportType.isNullOrBlank()) {
                        Text(
                            text = result.reportType,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    if (statusText.isNotBlank()) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                IconButton(onClick = { showShareDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onView,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Report")
                }
                OutlinedButton(
                    onClick = onDownload,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Download")
                }
            }
        }
    }

    if (showShareDialog) {
        Dialog(onDismissRequest = { showShareDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Share Result",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onCopyLink()
                            showShareDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Copy Link")
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            onDownloadPdf()
                            showShareDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Download PDF")
                    }
                }
            }
        }
    }
}
