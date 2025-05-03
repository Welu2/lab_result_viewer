package com.example.labresultviewer.network

import com.example.labresultviewer.model.LabResult
import retrofit2.http.GET

interface LabResultService {
    @GET("/lab-results")
    suspend fun getLabResults(): List<LabResult>
}
