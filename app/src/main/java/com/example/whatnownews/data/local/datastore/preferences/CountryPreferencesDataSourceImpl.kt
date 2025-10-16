package com.example.whatnownews.data.local.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CountryPreferencesDataSourceImpl(
    private val preferences: DataStore<Preferences>
) : CountryPreferencesDataSource {

    companion object {
        val SELECTED_COUNTRY = stringPreferencesKey("selectedCountry")
        val IS_DARK = booleanPreferencesKey("isDark")
    }

    override suspend fun saveCountry(countryCode: String) {
        preferences.edit { it ->
            it[SELECTED_COUNTRY] = countryCode
        }
    }

    override suspend fun toggleDarkMode() {
        preferences.edit { it ->
            it[IS_DARK] = !(it[IS_DARK] ?: true)
        }
    }

    override fun getCountry(): Flow<String> {
        return preferences.data.map {
            it[SELECTED_COUNTRY] ?: "us"
        }
    }

    override fun isDarkMode() : Flow<Boolean>{
        return preferences.data.map {
            it[IS_DARK] ?: true
        }
    }
}