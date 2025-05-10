package com.example.labresultviewer.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val _userRole = MutableStateFlow<String?>(sharedPreferences.getString("user_role", null))
    val userRole: StateFlow<String?> = _userRole

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun saveUserRole(role: String) {
        sharedPreferences.edit().putString("user_role", role).apply()
        _userRole.value = role
    }

    fun getUserRole(): String? {
        return _userRole.value
    }
}