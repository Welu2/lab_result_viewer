package com.example.labresultviewer.viewmodel.labresults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.repository.LabResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LabResultsViewModel @Inject constructor(
    private val repository: LabResultRepository,
    val sessionManager: SessionManager
) : ViewModel() {

    private val _labResults = MutableStateFlow<List<LabResult>>(emptyList())
    val labResults: StateFlow<List<LabResult>> = _labResults

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _uploadResult = MutableStateFlow<Response<LabResult>?>(null)
    val uploadResult: StateFlow<Response<LabResult>?> = _uploadResult

    fun loadLabResults() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val token = sessionManager.getToken()
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    _labResults.value = repository.fetchLabResults(bearerToken)
                } else {
                    _labResults.value = emptyList()
                }
            } catch (e: Exception) {
                // Handle error (e.g., emit empty list or error message)
                _labResults.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun uploadLabReport(patientId: String, file: MultipartBody.Part, testType: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val token = sessionManager.getToken()
                if (token == null) {
                    _uploadResult.value = null
                    return@launch
                }
                val testTypeBody = testType.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = repository.uploadLabReport(patientId, file, testTypeBody, "Bearer $token")
                _uploadResult.value = response
            } catch (e: Exception) {
                _uploadResult.value = null
            } finally {
                _loading.value = false
            }
        }
    }
    fun sendLabResultToUser(patientId: String) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                repository.sendLabResultToUser(patientId, "Bearer $token")
            }
        }
    }
} 