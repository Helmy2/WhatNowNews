package com.example.whatnownews.data.repository.auth

import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.domain.repository.auth.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) :
    AuthRepository {

    override fun signUp(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.sendEmailVerification()?.await()
        emit(Resource.Success(Unit))
    }.catch { e ->
        val message = when (e) {
            is FirebaseAuthException -> when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "The email address is already in use by another account."
                "ERROR_WEAK_PASSWORD" -> "The password is too weak. Please use a stronger password."
                else -> e.localizedMessage ?: "An unknown error occurred."
            }

            else -> e.localizedMessage ?: "An unknown error occurred."
        }
        emit(Resource.Error(message))
    }

    override fun sendEmailVerification(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = firebaseAuth.currentUser
        if (user == null) {
            emit(Resource.Error("No authenticated user found"))
            return@flow
        }
        user.sendEmailVerification().await()
        emit(Resource.Success(Unit))
    }.catch { e ->
        val message = e.localizedMessage ?: "Failed to send verification email"
        emit(Resource.Error(message))
    }

    override fun isEmailVerified(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        val user = firebaseAuth.currentUser
        if (user == null) {
            emit(Resource.Error("No authenticated user found"))
            return@flow
        }
        user.reload().await()
        emit(Resource.Success(user.isEmailVerified))
    }.catch { e ->
        val message = e.localizedMessage ?: "Failed to check verification status"
        emit(Resource.Error(message))
    }

    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Failed to send reset email.")
        }
    }

    override fun getCurrentUserUid(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

}