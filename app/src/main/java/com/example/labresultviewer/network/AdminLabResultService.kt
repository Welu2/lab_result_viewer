package com.example.labresultviewer.network

import com.example.labresultviewer.model.LabResult
import retrofit2.Response
import retrofit2.http.*

interface AdminLabResultService {
    @GET("/lab-results/admin")
    suspend fun getAllLabResults(
        @Header("Authorization") token: String
    ): List<LabResult>

    @PATCH("/lab-results/{id}")
    suspend fun updateLabResult(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body update: Map<String, String>
    ): Response<LabResult>

    @DELETE("/lab-results/{id}")
    suspend fun deleteLabResult(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>
}