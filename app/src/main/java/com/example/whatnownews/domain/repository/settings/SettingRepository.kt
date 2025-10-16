package com.example.whatnownews.domain.repository.settings

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun saveSelectedCountry(countryCode: String)
    fun getSelectedCountry(): Flow<String>

    fun isDarkMode(): Flow<Boolean>
    suspend fun toggleDarkMode()
}