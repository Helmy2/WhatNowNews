package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.usecase.auth.LoginUseCase
import com.example.whatnownews.domain.usecase.auth.CheckEmailVerifiedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    object EmailNotVerified : LoginUiState()
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val checkEmailVerifiedUseCase: CheckEmailVerifiedUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            // Attempt login
            val result = loginUseCase(email, password)

            when (result) {
                is Resource.Success -> {
                    // Check if email is verified
                    checkEmailVerifiedUseCase().collect { verificationResult ->
                        when (verificationResult) {
                            is Resource.Success -> {
                                if (verificationResult.data == true) {
                                    _loginState.value = LoginUiState.Success("Login successful!")
                                } else {
                                    _loginState.value = LoginUiState.EmailNotVerified
                                }
                            }
                            is Resource.Error -> {
                                _loginState.value = LoginUiState.Error(verificationResult.message ?: "Error checking verification status")
                            }
                            is Resource.Loading -> {
                                // Keep loading state
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    _loginState.value = LoginUiState.Error(result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    // Already handled by the initial state change
                }
            }
        }
    }
}