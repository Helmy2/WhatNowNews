package com.example.whatnownews.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.presentation.auth.repository.AuthRepository
import com.example.whatnownews.presentation.auth.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel for the SignUpFragment.
 *
 * @param authRepository The repository for authentication-related data operations.
 */
class SignUpViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Holds the state of the sign-up operation. A null value represents the initial state.
    private val _signUpState = MutableStateFlow<Resource<AuthResult>?>(null)
    val signUpState: StateFlow<Resource<AuthResult>?> = _signUpState

    /**
     * Signs up a new user with the given email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param confirmPassword The user's confirmed password.
     */
    fun signUp(email: String, password: String, confirmPassword: String) {
        // Validate that the passwords match.
        if (password != confirmPassword) {
            _signUpState.value = Resource.Error("Passwords do not match")
            return
        }

        // Call the signUp method of the AuthRepository and update the state.
        authRepository.signUp(email, password).onEach { result ->
            _signUpState.value = result
        }.launchIn(viewModelScope)
    }
}
