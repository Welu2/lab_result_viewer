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

    fun loadProfile() {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                _profile.value = userRepository.getProfile("Bearer $token")
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
}