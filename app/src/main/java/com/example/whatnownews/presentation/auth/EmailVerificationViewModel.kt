package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.usecase.auth.CheckEmailVerifiedUseCase
import com.example.whatnownews.domain.usecase.auth.SendEmailVerificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EmailVerificationViewModel(
    private val sendEmailVerification: SendEmailVerificationUseCase,
    private val checkEmailVerified: CheckEmailVerifiedUseCase
) : ViewModel() {

    private val _resendState = MutableStateFlow<Resource<Unit>?>(null)
    val resendState: StateFlow<Resource<Unit>?> = _resendState

    private val _checkState = MutableStateFlow<Resource<Boolean>?>(null)
    val checkState: StateFlow<Resource<Boolean>?> = _checkState

    fun resendVerification() {
        sendEmailVerification().onEach { result ->
            _resendState.value = result
        }.launchIn(viewModelScope)
    }

    fun checkVerificationStatus() {
        checkEmailVerified().onEach { result ->
            _checkState.value = result
        }.launchIn(viewModelScope)
    }
}

