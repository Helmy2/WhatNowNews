package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.usecase.auth.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel for the SignUpFragment.
 *
 * @param signUpUseCase Use case to sign up a new user.
 */
class SignUpViewModel(private val signUpUseCase: SignUpUseCase) : ViewModel() {

    // Holds the state of the sign-up operation. A null value represents the initial state.
    private val _signUpState = MutableStateFlow<Resource<Unit>?>(null)
    val signUpState: StateFlow<Resource<Unit>?> = _signUpState

    /**
     * Signs up a new user with the given email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param confirmPassword The user's confirmed password.
     */
    fun signUp(email: String, password: String, confirmPassword: String) {
        signUpUseCase(email, password, confirmPassword).onEach { result ->
            _signUpState.value = result
        }.launchIn(viewModelScope)
    }
}
