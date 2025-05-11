package com.example.labresultviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.data.SessionManager
import com.example.labresultviewer.model.Notification
import com.example.labresultviewer.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    val unreadCount = _notifications.map { list -> list.count { !it.isRead } }

    fun loadNotifications() {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val bearerToken = "Bearer $token"
                _notifications.value = repository.getUserNotifications(bearerToken)
            }
        }
    }

    fun markAsRead(id: Int) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val bearerToken = "Bearer $token"
                repository.markAsRead(bearerToken, id)
                // Refresh notifications
                loadNotifications()
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            val unread = _notifications.value.filter { !it.isRead }
            unread.forEach { markAsRead(it.id) }
        }
    }
} 