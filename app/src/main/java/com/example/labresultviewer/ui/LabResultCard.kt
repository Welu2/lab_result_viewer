package com.example.labresultviewer.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
fun LabResultCard(result: LabResult) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = result.testName, fontSize = 12.sp)
            Text(text = result.date, fontSize = 10.sp)
            Text(text = result.status, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { /* TODO: view report */ }) {
                    Text("View Report")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = { /* TODO: download PDF */ }) {
                    Text("Download")
                }
            }
        }
    }
}
