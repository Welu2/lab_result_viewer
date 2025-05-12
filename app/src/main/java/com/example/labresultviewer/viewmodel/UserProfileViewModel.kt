package com.example.labresultviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.model.UserProfile
import com.example.labresultviewer.repository.UserRepository
import com.example.labresultviewer.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _emailChangeState = MutableStateFlow<Result<Boolean>>(Result.success(false))
    val emailChangeState: StateFlow<Result<Boolean>> = _emailChangeState

    fun loadProfile() {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                _profile.value = userRepository.getProfile("Bearer $token")
            }
        }
    }

    fun changeEmail(newEmail: String, password: String) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                try {
                    val response = userRepository.updateEmail(
                        token = "Bearer $token",
                        email = newEmail,
                        password = password
                    )
                    if (response.isSuccessful) {
                        _emailChangeState.value = Result.success(true)
                        // Reload profile to get updated email
                        loadProfile()
                    } else {
                        _emailChangeState.value = Result.failure(Exception("Failed to change email"))
                    }
                } catch (e: Exception) {
                    _emailChangeState.value = Result.failure(e)
                }
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    suspend fun deleteProfile(): Boolean {
        return try {
            val token = sessionManager.getToken()
            val profileId = profile.value?.id ?: return false
            if (token != null) {
                val response = userRepository.deleteProfile(profileId, "Bearer $token")
                response.isSuccessful
            } else false
        } catch (e: Exception) {
            false
        }
    }

    fun updateProfile(updates: Map<String, String>) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            val profileId = profile.value?.id ?: return@launch
            if (token != null) {
                val response = userRepository.updateProfile("Bearer $token", profileId, updates)
                if (response.isSuccessful) {
                    loadProfile() // Refresh profile
                }
            }
        }
    }
}