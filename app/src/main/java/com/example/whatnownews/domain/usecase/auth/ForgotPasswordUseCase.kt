package com.example.whatnownews.domain.usecase.auth

import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.repository.AuthRepository

class ForgotPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Resource<Unit> {
        if (email.isBlank()) {
            return Resource.Error("Email cannot be empty.")
        }
        // Basic email validation could go here, or a separate validator
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error("Invalid email format.")
        }

        return repository.sendPasswordResetEmail(email)
    }
}