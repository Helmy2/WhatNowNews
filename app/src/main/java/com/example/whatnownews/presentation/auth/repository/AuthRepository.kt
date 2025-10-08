package com.example.whatnownews.presentation.auth.repository

import com.example.whatnownews.presentation.auth.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for authentication-related data operations.
 */
interface AuthRepository {
    /**
     * Creates a new user with the given email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A Flow that emits the result of the sign-up operation.
     */
    fun signUp(email: String, password: String): Flow<Resource<AuthResult>>
}
