package com.example.labresultviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.repository.AdminLabResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminLabResultViewModel @Inject constructor(
    private val repository: AdminLabResultRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _labResults = MutableStateFlow<List<LabResult>>(emptyList())
    val labResults: StateFlow<List<LabResult>> = _labResults

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadLabResults() {
        viewModelScope.launch {
            _loading.value = true
            val token = sessionManager.getToken()
            if (token != null) {
                _labResults.value = repository.getAllLabResults("Bearer $token")
            }
            _loading.value = false
        }
    }

    fun updateLabResult(id: Int, update: Map<String, String>, onDone: () -> Unit) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                repository.updateLabResult("Bearer $token", id, update)
                loadLabResults()
                onDone()
            }
        }
    }

    fun deleteLabResult(id: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                repository.deleteLabResult("Bearer $token", id)
                loadLabResults()
                onDone()
            }
        }
    }
}