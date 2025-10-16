package com.example.whatnownews.data.repository.settings

import com.example.whatnownews.data.local.datastore.preferences.CountryPreferencesDataSource
import com.example.whatnownews.domain.repository.settings.SettingRepository
import kotlinx.coroutines.flow.Flow

class SettingRepositoryImpl(
    private val preferencesDataSource: CountryPreferencesDataSource
) : SettingRepository {

    override suspend fun saveSelectedCountry(countryCode: String) {
        preferencesDataSource.saveCountry(countryCode)
    }

    override fun getSelectedCountry(): Flow<String> {
        return preferencesDataSource.getCountry()
    }

    override fun isDarkMode(): Flow<Boolean> {
        return preferencesDataSource.isDarkMode()
    }

    override suspend fun toggleDarkMode(){
        preferencesDataSource.toggleDarkMode()
    }

}