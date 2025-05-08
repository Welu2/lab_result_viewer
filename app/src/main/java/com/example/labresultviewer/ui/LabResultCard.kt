package com.example.labresultviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labresultviewer.model.LabResult

@Composable
fun LabResultCard(result: LabResult, onView: () -> Unit = {}, onDownload: () -> Unit = {}) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = result.title, fontSize = 16.sp)
            if (!result.reportDate.isNullOrBlank())
                Text(text = result.reportDate, fontSize = 12.sp)
            if (!result.reportType.isNullOrBlank())
                Text(text = result.reportType, fontSize = 12.sp)
            if (!result.status.isNullOrBlank())
                Text(text = result.status, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onView) {
                    Text("View Report")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onDownload, enabled = !result.downloadUrl.isNullOrBlank()) {
                    Text("Download")
                }
            }
        }
    }
}
