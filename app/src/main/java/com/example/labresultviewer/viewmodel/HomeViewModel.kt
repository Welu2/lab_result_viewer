// viewmodel/HomeViewModel.kt
package com.example.labresultviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.network.ApiService
import com.example.labresultviewer.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.labresultviewer.repository.LabResultRepository
import com.example.labresultviewer.model.LabResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val labResultRepository: LabResultRepository
) : ViewModel() {

    private val _profileName = MutableStateFlow("")
    val profileName: StateFlow<String> = _profileName

    private val _labResults = MutableStateFlow<List<LabResult>>(emptyList())
    val labResults: StateFlow<List<LabResult>> = _labResults

    val totalTests: StateFlow<Int> = _labResults.map { it.size }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    init {
        viewModelScope.launch {
            try {
                val token = sessionManager.getToken()
                val bearerToken = "Bearer $token"

                val response = apiService.getProfile(bearerToken)

                if (response.isSuccessful) {
                    val profile = response.body()
                    _profileName.value = profile?.name ?: "Unknown"
                } else {
                    println("Error fetching profile: ${response.code()} - ${response.message()}")
                }

                // Fetch lab results with token
                if (token != null) {
                    val results = labResultRepository.fetchLabResults("Bearer $token")
                    _labResults.value = results
                }
            } catch (e: Exception) {
                println("Error in HomeViewModel init: ${e.message}")
            }
        }
    }
}
