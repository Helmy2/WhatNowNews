package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthStatus {
    object Loading : AuthStatus()
    object Authenticated : AuthStatus()
    object Unauthenticated : AuthStatus()
}

class SplashViewModel(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase // Injected by Koin
) : ViewModel() {

    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Loading)
    val authStatus = _authStatus.asStateFlow()

    init {
        // Run the check immediately upon ViewModel creation
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val isLoggedIn = checkAuthStatusUseCase()
        _authStatus.value = if (isLoggedIn) {
            AuthStatus.Authenticated
        } else {
            AuthStatus.Unauthenticated
        }
    }
}