package com.example.labresultviewer.model

data class TestResult(
    val testName: String,
    val resultValue: String,
    val normalRange: String,
    val isAbnormal: Boolean,
    val date: String
)
