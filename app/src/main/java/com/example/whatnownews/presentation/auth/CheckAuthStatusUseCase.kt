package com.example.whatnownews.presentation.auth

import com.example.whatnownews.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth

class CheckAuthStatusUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean {
        // Returns true only if user is logged in AND email is verified
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null && currentUser.isEmailVerified
    }
}