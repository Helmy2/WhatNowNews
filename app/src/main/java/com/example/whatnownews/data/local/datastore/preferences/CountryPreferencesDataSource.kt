package com.example.whatnownews.data.local.datastore.preferences

import kotlinx.coroutines.flow.Flow

interface CountryPreferencesDataSource {
    suspend fun saveCountry(countryCode: String)
    fun getCountry(): Flow<String>
    fun isDarkMode(): Flow<Boolean>
    suspend fun toggleDarkMode()
}