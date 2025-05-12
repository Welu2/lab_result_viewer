package com.example.labresultviewer.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.labresultviewer.viewmodel.labresults.LabResultsViewModel
import kotlinx.coroutines.launch
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

suspend fun downloadAndOpenPdf(
    context: Context,
    labResultId: Int,
    token: String
) {
    val url = "http://192.168.100.7:3001/lab-results/download/$labResultId"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer $token")
        .build()

    withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val inputStream = response.body?.byteStream()
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "lab_result_$labResultId.pdf"
                )
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()

                withContext(Dispatchers.Main) {
                    val uri = Uri.fromFile(file)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/pdf")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "No app found to open PDF", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Download failed: ${response.code}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabResultsScreen(viewModel: LabResultsViewModel) {
    val labResults by viewModel.labResults.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val token = viewModel.sessionManager.getToken() ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadLabResults()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lab Results") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                labResults.isEmpty() -> {
                    Text(
                        text = "No Lab Results Found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(labResults) { result ->
                            LabResultCard(
                                result = result,
                                onView = {
                                    result.downloadUrl?.let { url ->
                                        // Open the PDF in the browser or PDF viewer
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("No app found to open this file")
                                            }
                                        }
                                    } ?: run {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("No report available to view")
                                        }
                                    }
                                },
                                onDownload = {
                                    coroutineScope.launch {
                                        downloadAndOpenPdf(context, result.id, token)
                                    }
                                },
                                onCopyLink = {
                                    val downloadUrl = "http://192.168.100.7:3001/lab-results/download/${result.id}"
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Lab Result Link", downloadUrl)
                                    clipboard.setPrimaryClip(clip)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Link copied to clipboard")
                                    }
                                },
                                onDownloadPdf = {
                                    coroutineScope.launch {
                                        downloadAndOpenPdf(context, result.id, token)
                                    }
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}