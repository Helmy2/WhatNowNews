package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            // Validate inputs here if needed

            val result = loginUseCase(email, password)
            _loginState.value = when (result) {
                is Resource.Success -> LoginUiState.Success("Login successful!")
                is Resource.Error -> LoginUiState.Error(result.message ?: "Unknown error")
                is Resource.Loading -> LoginUiState.Loading // This case is handled by the initial state change
            }
        }
    }
}