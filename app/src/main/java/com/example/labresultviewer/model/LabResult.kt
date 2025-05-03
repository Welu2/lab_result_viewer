package com.example.labresultviewer.model

data class LabResult(
    val id: String,
    val testName: String,
    val date: String,
    val status: String,
    val downloadUrl: String
)
