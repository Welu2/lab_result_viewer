package com.example.labresultviewer.ui.labresults

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.labresultviewer.viewmodel.labresults.LabResultsViewModel
import com.example.labresultviewer.ui.LabResultCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabResultsScreen(viewModel: LabResultsViewModel) {
    val labResults by viewModel.labResults.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLabResults()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lab Results") })
        }
    ) { paddingValues ->
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (labResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No Lab Results Found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(labResults) { result ->
                    LabResultCard(result)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}