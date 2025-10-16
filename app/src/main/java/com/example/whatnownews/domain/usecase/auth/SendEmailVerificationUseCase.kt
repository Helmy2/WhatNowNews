package com.example.whatnownews.domain.usecase.auth

import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow

class SendEmailVerificationUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Resource<Unit>> = repository.sendEmailVerification()
}

