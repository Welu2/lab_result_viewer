package com.example.labresultviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labresultviewer.model.User
import com.example.labresultviewer.repository.AuthRepository
import com.example.labresultviewer.repository.UserWithToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // For login flow
    sealed class AuthResult {
        object Idle : AuthResult()
        object Loading : AuthResult()
        data class Success(val user: UserWithToken) : AuthResult()
        data class Error(val message: String?) : AuthResult()
    }

    private val _loginState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val loginState: StateFlow<AuthResult> = _loginState

    // For registration flow
    sealed class RegistrationResult {
        object Idle : RegistrationResult()
        object Loading : RegistrationResult()
        data class Success(val user: UserWithToken) : RegistrationResult()
        data class Error(val message: String?) : RegistrationResult()
    }

    private val _registrationState = MutableStateFlow<RegistrationResult>(RegistrationResult.Idle)
    val registrationState: StateFlow<RegistrationResult> = _registrationState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthResult.Loading
            try {
                repository.login(email, password)
                    .onSuccess { userWithToken ->
                        _loginState.value = AuthResult.Success(userWithToken)
                    }
                    .onFailure { e ->
                        _loginState.value = AuthResult.Error(e.message)
                    }
            } catch (e: Exception) {
                _loginState.value = AuthResult.Error(e.message)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = RegistrationResult.Loading
            try {
                repository.register(email, password)
                    .onSuccess { userWithToken ->
                        println("Registration SUCCESS: $userWithToken")
                        _registrationState.value = RegistrationResult.Success(userWithToken)
                    }
                    .onFailure { e ->
                        _registrationState.value = RegistrationResult.Error(e.message)
                    }
            } catch (e: Exception) {
                _registrationState.value = RegistrationResult.Error(e.message)
            }
        }
    }
}

// Add this sealed class for better state handling
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
