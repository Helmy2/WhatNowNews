package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.usecase.auth.CheckEmailVerifiedUseCase
import com.example.whatnownews.domain.usecase.auth.SendEmailVerificationUseCase
import com.example.whatnownews.domain.usecase.auth.SignOutUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val sendEmailVerification: SendEmailVerificationUseCase,
    private val checkEmailVerified: CheckEmailVerifiedUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _resendState = MutableStateFlow<Resource<Unit>?>(null)
    val resendState: StateFlow<Resource<Unit>?> = _resendState

    private val _checkState = MutableStateFlow<Resource<Boolean>?>(null)
    val checkState: StateFlow<Resource<Boolean>?> = _checkState

    private val _timerSeconds = MutableStateFlow(60)
    val timerSeconds: StateFlow<Int> = _timerSeconds

    private val _isResendEnabled = MutableStateFlow(false)
    val isResendEnabled: StateFlow<Boolean> = _isResendEnabled

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    fun resendVerification() {
        sendEmailVerification().onEach { result ->
            _resendState.value = result
            if (result is Resource.Success) {
                startTimer()
            }
        }.launchIn(viewModelScope)
    }

    fun checkVerificationStatus() {
        checkEmailVerified().onEach { result ->
            _checkState.value = result
        }.launchIn(viewModelScope)
    }

    fun signOutUnverifiedUser() {
        signOutUseCase()
    }

    private fun startTimer() {
        timerJob?.cancel()
        _timerSeconds.value = 60
        _isResendEnabled.value = false

        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0) {
                delay(1000)
                _timerSeconds.value--
            }
            _isResendEnabled.value = true
        }
    }

    fun getUserEmail(): String? {
        return com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
