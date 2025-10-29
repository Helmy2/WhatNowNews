package com.example.whatnownews.domain.usecase.auth

import com.example.whatnownews.domain.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.signOut()
}

