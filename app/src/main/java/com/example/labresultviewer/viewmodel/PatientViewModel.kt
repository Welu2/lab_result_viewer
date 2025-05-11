package com.example.labresultviewer.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.Appointment
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.model.RegisterRequest
import com.example.labresultviewer.network.ApiService
import com.example.labresultviewer.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    private val _patients = mutableStateListOf<UserProfile>()
    val patients: List<UserProfile> = _patients

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _bookingResult = mutableStateOf<retrofit2.Response<Appointment>?>(null)
    val bookingResult: State<retrofit2.Response<Appointment>?> = _bookingResult

    private val _appointments = mutableStateListOf<Appointment>()
    val appointments: List<Appointment> = _appointments

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
                Log.d("PATIENTS_DEBUG", "API Response: ${response.code()}")
                if (response.isSuccessful) {
                    _patients.clear()
                    response.body()?.let { patients ->
                        Log.d("PATIENTS_DEBUG", "Patients received: ${patients.size}")
                        _patients.addAll(patients)
                    }
                } else {
                    Log.e("PATIENTS_DEBUG", "API Error: ${response.errorBody()?.string()}")
                    _error.value = "Failed to load patients: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("PATIENTS_DEBUG", "Network error: ${e.message}", e)
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addPatient(patientData: Map<String, String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sessionManager.getToken() ?: run {
                _error.value = "Not authenticated"
                _isLoading.value = false
                return@launch
            }

            try {
                // First register the user
                val registerRequest = RegisterRequest(
                    email = patientData["email"] ?: "",
                    password = patientData["password"] ?: "",
                    patientId = null // The backend will generate this
                )
                Log.d("PATIENTS_DEBUG", "Registering user with email: ${registerRequest.email}")
                val registerResponse = apiService.register(registerRequest)
                Log.d("PATIENTS_DEBUG", "Register response code: ${registerResponse.code()}")
                Log.d("PATIENTS_DEBUG", "Register response body: ${registerResponse.body()}")
                Log.d("PATIENTS_DEBUG", "Register error body: ${registerResponse.errorBody()?.string()}")

                if (registerResponse.isSuccessful) {
                    // Get the new user's token and patient ID
                    val authResponse = registerResponse.body()
                    val newUserToken = authResponse?.token?.accessToken
                    val newPatientId = authResponse?.user?.patientId

                    if (newUserToken != null) {
                        // Then create the profile using the new user's token
                        Log.d("PATIENTS_DEBUG", "Creating profile with data: $patientData")
                        val profileResponse = apiService.createProfile(
                            token = "Bearer $newUserToken",
                            profileData = patientData + mapOf("patientId" to (newPatientId ?: ""))
                        )
                        Log.d("PATIENTS_DEBUG", "Profile response code: ${profileResponse.code()}")
                        Log.d("PATIENTS_DEBUG", "Profile response body: ${profileResponse.body()}")
                        Log.d("PATIENTS_DEBUG", "Profile error body: ${profileResponse.errorBody()?.string()}")

                        if (profileResponse.isSuccessful) {
                            // Reload the patients list
                            loadPatients()
                        } else {
                            _error.value = "Failed to create profile: ${profileResponse.code()}"
                        }
                    } else {
                        _error.value = "Failed to get new user token"
                    }
                } else {
                    _error.value = "Failed to register user: ${registerResponse.code()}"
                }
            } catch (e: Exception) {
                Log.e("PATIENTS_DEBUG", "Add patient error: ${e.message}", e)
                _error.value = "Error adding patient: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun editPatient(patientId: String, patientData: Map<String, String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sessionManager.getToken() ?: run {
                _error.value = "Not authenticated"
                _isLoading.value = false
                return@launch
            }

            try {
                // Extract numeric ID from PAT-XXXXX format
                val numericId = patientId.replace("PAT-", "").toIntOrNull()
                if (numericId == null) {
                    _error.value = "Invalid patient ID format"
                    _isLoading.value = false
                    return@launch
                }

                Log.d("PATIENTS_DEBUG", "Updating profile for patientId: $patientId (numeric ID: $numericId)")
                Log.d("PATIENTS_DEBUG", "Update data: $patientData")
                
                val response = apiService.updateProfile(
                    token = "Bearer $token",
                    id = numericId,
                    profileData = patientData
                )
                
                Log.d("PATIENTS_DEBUG", "Update response code: ${response.code()}")
                Log.d("PATIENTS_DEBUG", "Update response body: ${response.body()}")
                Log.d("PATIENTS_DEBUG", "Update error body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    // Reload the patients list
                    loadPatients()
                } else {
                    _error.value = "Failed to update profile: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("PATIENTS_DEBUG", "Edit patient error: ${e.message}", e)
                _error.value = "Error updating patient: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPatientById(patientId: String, onSuccess: (UserProfile) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sessionManager.getToken() ?: run {
                _error.value = "Not authenticated"
                _isLoading.value = false
                return@launch
            }

            try {
                val response = apiService.getPatientById("Bearer $token", patientId)
                if (response.isSuccessful) {
                    response.body()?.let { profile ->
                        onSuccess(profile)
                    }
                } else {
                    _error.value = "Failed to get patient: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("PATIENTS_DEBUG", "Get patient error: ${e.message}", e)
                _error.value = "Error getting patient: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserAppointments() {
        viewModelScope.launch {
            val token = sessionManager.getToken() ?: return@launch
            try {
                val result = appointmentRepository.getUserAppointments("Bearer $token")
                _appointments.clear()
                _appointments.addAll(result)
            } catch (e: Exception) {
                _appointments.clear()
            }
        }
    }

    fun bookAppointment(testType: String, date: String, time: String) {
        viewModelScope.launch {
            val token = sessionManager.getToken() ?: return@launch
            try {
                val response = appointmentRepository.bookAppointment("Bearer $token", testType, date, time)
                _bookingResult.value = response
                if (response.isSuccessful) {
                    loadUserAppointments()
                }
            } catch (e: Exception) {
                _bookingResult.value = null
            }
        }
    }

    fun clearBookingResult() {
        _bookingResult.value = null
    }

    fun deleteAppointment(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val token = sessionManager.getToken() ?: return@launch
            try {
                val response = appointmentRepository.deleteAppointment("Bearer $token", id)
                if (response.isSuccessful) {
                    loadUserAppointments()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}