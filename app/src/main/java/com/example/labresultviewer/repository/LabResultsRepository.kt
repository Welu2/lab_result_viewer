package com.example.labresultviewer.repository

import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.network.LabResultService

class LabResultRepository(private val service: LabResultService) {
    suspend fun fetchLabResults(): List<LabResult> {
        return service.getLabResults()
    }
}
