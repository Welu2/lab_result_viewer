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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileName = MutableStateFlow("")
    val profileName: StateFlow<String> = _profileName

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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
