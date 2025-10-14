package com.example.whatnownews.presentation.auth

import com.example.whatnownews.domain.repository.AuthRepository

class CheckAuthStatusUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean {
        // Returns true if the UID is not null (user is logged in)
        return repository.getCurrentUserUid() != null
    }
}