package com.example.labresultviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.Appointment
import com.example.labresultviewer.repository.AdminAppointmentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAppointmentsViewModel @Inject constructor(
    private val repository: AdminAppointmentsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _pendingAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    val pendingAppointments: StateFlow<List<Appointment>> = _pendingAppointments

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadPendingAppointments() {
        viewModelScope.launch {
            _loading.value = true
            val token = sessionManager.getToken()
            if (token != null) {
                _pendingAppointments.value = repository.getPendingAppointments("Bearer $token")
            }
            _loading.value = false
        }
    }

    fun approve(id: Int) {
        updateStatus(id, "confirmed")
    }

    fun decline(id: Int) {
        updateStatus(id, "disapproved")
    }

    private fun updateStatus(id: Int, status: String) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                repository.updateAppointmentStatus("Bearer $token", id, status)
                loadPendingAppointments()
            }
        }
    }
}