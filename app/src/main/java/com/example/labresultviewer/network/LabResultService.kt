package com.example.labresultviewer.network

import com.example.labresultviewer.model.LabResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface LabResultService {
    @GET("/lab-results")
    suspend fun getLabResults(
        @Header("Authorization") token: String
    ): List<LabResult>

    @Multipart
    @POST("/lab-results/upload/{patientId}")
    suspend fun uploadLabReport(
        @Path("patientId") patientId: String,
        @Part file: MultipartBody.Part,
        @Part("testType") testType: RequestBody,
        @Header("Authorization") token: String
    ): Response<LabResult>

    @POST("/lab-results")
    suspend fun createLabResult(
        @Body labResult: LabResult
    ): Response<LabResult>

    @POST("/lab-results/send/{patientId}")
    suspend fun sendLabResultToUser(
        @Path("patientId") patientId: String,
        @Header("Authorization") token: String
    ): Response<Unit>
}
