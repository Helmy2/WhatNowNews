package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.usecase.auth.ForgotPasswordUseCase
import com.example.whatnownews.presentation.auth.ForgotPasswordUiState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ForgotPasswordUiState {
    object Idle : ForgotPasswordUiState()
    object Loading : ForgotPasswordUiState()
    object Success : ForgotPasswordUiState() // Simplified for one-time event
    data class Error(val message: String) : ForgotPasswordUiState()
}

class ForgotPasswordViewModel(
    // Injected by Koin
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun sendResetLink(email: String) {
        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading

            when (val result = forgotPasswordUseCase(email)) {
                is Resource.Success -> {
                    // Post a Success state, then immediately reset to Idle
                    // to ensure the UI only reacts once.
                    _uiState.value = ForgotPasswordUiState.Success
                    _uiState.value = ForgotPasswordUiState.Idle
                }
                is Resource.Error -> {
                    _uiState.value = Error(result.message ?: "Unknown error")
                    // Reset to Idle after showing the error
                    // You might keep the error state for a bit depending on UX
                    _uiState.value = ForgotPasswordUiState.Idle
                }

                is Resource.Loading<*> -> TODO()
            }
        }
    }
}