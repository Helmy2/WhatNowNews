package com.example.whatnownews.domain.repository

import com.example.whatnownews.core.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Domain abstraction for authentication operations.
 */
interface AuthRepository {
    /**
     * Creates a new user and sends a verification email.
     * Returns success when the email is sent.
     */
    fun signUp(email: String, password: String): Flow<Resource<Unit>>

    /**
     * Resend the email verification to the current user.
     */
    fun sendEmailVerification(): Flow<Resource<Unit>>

    /**
     * Check whether the current user's email is verified.
     */
    fun isEmailVerified(): Flow<Resource<Boolean>>
}
