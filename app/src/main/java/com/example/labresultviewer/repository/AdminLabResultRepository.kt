package com.example.labresultviewer.repository

import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.network.AdminLabResultService
import retrofit2.Response
import javax.inject.Inject

class AdminLabResultRepository @Inject constructor(
    private val service: AdminLabResultService
) {
    suspend fun getAllLabResults(token: String): List<LabResult> =
        service.getAllLabResults(token)

    suspend fun updateLabResult(token: String, id: Int, update: Map<String, String>): Response<LabResult> =
        service.updateLabResult(token, id, update)

    suspend fun deleteLabResult(token: String, id: Int): Response<Unit> =
        service.deleteLabResult(token, id)
}