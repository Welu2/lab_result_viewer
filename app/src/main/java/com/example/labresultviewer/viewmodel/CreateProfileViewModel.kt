package com.example.labresultviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.CreateProfileRequest
import com.example.labresultviewer.network.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val sessionManager: SessionManager
) : ViewModel() {

    // LiveData or State for UI
    private val _createProfileState = MutableLiveData<Result<Boolean>>()
    val createProfileState: LiveData<Result<Boolean>> = _createProfileState

    fun createProfile(createProfileRequest: CreateProfileRequest) {
        viewModelScope.launch {
            val token = sessionManager.getToken() // Get the JWT token from SessionManager

            if (token.isNullOrEmpty()) {
                _createProfileState.value = Result.failure(Exception("No valid token"))
                return@launch
            }

            // Call the createProfile API
            val response = authService.createProfile(createProfileRequest, "Bearer $token")

            if (response.isSuccessful) {
                // Successfully created the profile
                _createProfileState.value = Result.success(true)
            } else {
                // Handle error response
                _createProfileState.value = Result.failure(Exception("Profile creation failed"))
            }
        }
    }
}
