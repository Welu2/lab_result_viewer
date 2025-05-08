package com.example.labresultviewer.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _patients = mutableStateListOf<UserProfile>()
    val patients: List<UserProfile> = _patients

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // In your PatientViewModel's loadPatients():
    fun loadPatients() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sessionManager.getToken() ?: run {
                _error.value = "Not authenticated"
                _isLoading.value = false
                return@launch
            }

            try {
                val response = apiService.getAllPatients("Bearer $token")
                Log.d("PATIENTS_DEBUG", "API Response: ${response.code()}") // Debug log
                if (response.isSuccessful) {
                    _patients.clear()
                    response.body()?.let { patients ->
                        Log.d("PATIENTS_DEBUG", "Patients received: ${patients.size}") // Debug log
                        _patients.addAll(patients)
                    }
                } else {
                    Log.e("PATIENTS_DEBUG", "API Error: ${response.errorBody()?.string()}") // Debug log
                }
            } catch (e: Exception) {
                Log.e("PATIENTS_DEBUG", "Network error: ${e.message}", e) // Debug log
            } finally {
                _isLoading.value = false
            }
        }
    }
}