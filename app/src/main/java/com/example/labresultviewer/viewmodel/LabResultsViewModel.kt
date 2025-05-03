package com.example.labresultviewer.viewmodel.labresults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.model.LabResult
import com.example.labresultviewer.repository.LabResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabResultsViewModel @Inject constructor(private val repository: LabResultRepository) : ViewModel() {

    private val _labResults = MutableStateFlow<List<LabResult>>(emptyList())
    val labResults: StateFlow<List<LabResult>> = _labResults

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadLabResults() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _labResults.value = repository.fetchLabResults()
            } catch (e: Exception) {
                // Handle error (e.g., emit empty list or error message)
                _labResults.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}
