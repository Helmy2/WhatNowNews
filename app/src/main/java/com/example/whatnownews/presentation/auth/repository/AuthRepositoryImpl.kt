package com.example.whatnownews.presentation.auth.repository

import com.example.whatnownews.presentation.auth.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

/**
 * Implementation of the AuthRepository interface that uses Firebase Authentication.
 *
 * @param firebaseAuth The FirebaseAuth instance.
 */
class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    /**
     * Creates a new user with the given email and password and sends a verification email.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A Flow that emits the result of the sign-up operation.
     */
    override fun signUp(email: String, password: String): Flow<Resource<AuthResult>> = flow {
        // Emit a loading state to indicate that the sign-up process has started.
        emit(Resource.Loading())

        // Create a new user with the given email and password.
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

        // Send a verification email to the new user.
        result.user?.sendEmailVerification()?.await()

        // Emit a success state with the result of the sign-up operation.
        emit(Resource.Success(result))
    }.catch { e ->
        // Handle Firebase-specific exceptions and emit a user-friendly error message.
        val message = when (e) {
            is FirebaseAuthException -> {
                when (e.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "The email address is already in use by another account."
                    "ERROR_WEAK_PASSWORD" -> "The password is too weak. Please use a stronger password."
                    else -> e.localizedMessage ?: "An unknown error occurred."
                }
            }
            else -> e.localizedMessage ?: "An unknown error occurred."
        }
        emit(Resource.Error(message))
    }
}
