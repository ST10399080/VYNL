package com.example.vynl.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl.data.remote.auth.FirebaseAuthService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authService = FirebaseAuthService()

    private val _currentUser =
        MutableStateFlow<FirebaseUser?>(authService.getCurrentUser())

    val currentUser: StateFlow<FirebaseUser?> =
        _currentUser.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()

    fun register(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null

            try {

                val user = authService.register(
                    email = email,
                    password = password
                )

                _currentUser.value = user

            } catch (exception: Exception) {

                _errorMessage.value =
                    exception.message ?: "Registration failed."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null

            try {

                val user = authService.login(
                    email = email,
                    password = password
                )

                _currentUser.value = user

            } catch (exception: Exception) {

                _errorMessage.value =
                    exception.message ?: "Login failed."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun logout() {

        authService.logout()

        _currentUser.value = null
        _errorMessage.value = null
    }

    fun clearError() {

        _errorMessage.value = null
    }
}