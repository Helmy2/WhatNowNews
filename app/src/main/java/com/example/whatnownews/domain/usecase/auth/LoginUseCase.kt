package com.example.whatnownews.domain.usecase.auth

import com.example.whatnownews.domain.repository.auth.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = authRepository.login(email, password)
}