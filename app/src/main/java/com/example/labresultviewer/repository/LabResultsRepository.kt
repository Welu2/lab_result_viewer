package com.example.labresultviewer.repository

import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.network.LabResultService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class LabResultRepository(private val service: LabResultService) {
    suspend fun fetchLabResults(token: String): List<LabResult> {
        return service.getLabResults(token)
    }

    suspend fun uploadLabReport(patientId: String, file: MultipartBody.Part, testType: RequestBody, token: String): Response<LabResult> {
        return service.uploadLabReport(patientId, file, testType, token)
    }

    suspend fun createLabResult(labResult: LabResult): Response<LabResult> {
        return service.createLabResult(labResult)
    }

    suspend fun sendLabResultToUser(patientId: String, token: String): Response<Unit> {
        return service.sendLabResultToUser(patientId, token)
    }
}
