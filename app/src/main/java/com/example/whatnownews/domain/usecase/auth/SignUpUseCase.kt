package com.example.whatnownews.domain.usecase.auth

import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpUseCase(private val repository: AuthRepository) {
    operator fun invoke(email: String, password: String, confirmPassword: String): Flow<Resource<Unit>> {
        if (password != confirmPassword) {
            return flow { emit(Resource.Error("Passwords do not match")) }
        }
        return repository.signUp(email, password)
    }
}

