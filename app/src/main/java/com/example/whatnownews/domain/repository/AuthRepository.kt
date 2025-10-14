package com.example.whatnownews.domain.repository

import com.example.whatnownews.core.util.Resource
import com.google.firebase.auth.AuthResult
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

    /**
     * Logs in an existing user.
     * Returns success when the login is successful.
     */
    suspend fun login(email: String, password: String): Resource<AuthResult>

    /**
     * Sends a password reset email to the user.
     * Returns success when the email is sent.
     */
    suspend fun sendPasswordResetEmail(email: String): Resource<Unit>

    /**
     * Get the current user's UID.
     */
    fun getCurrentUserUid(): String?
}
