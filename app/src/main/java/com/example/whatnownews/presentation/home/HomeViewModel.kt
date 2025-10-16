package com.example.whatnownews.presentation.home

import androidx.lifecycle.ViewModel
import com.example.whatnownews.domain.repository.auth.AuthRepository

class HomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun logout() {
        authRepository.signOut()
    }
}