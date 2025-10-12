package com.example.whatnownews.domain.usecase.auth

import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class CheckEmailVerifiedUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Resource<Boolean>> = repository.isEmailVerified()
}

