package com.example.whatnownews.data.repository

import com.example.whatnownews.data.preferences.CountryPreferencesDataSource
import com.example.whatnownews.domain.repository.CountryRepository

class CountryRepositoryImpl(
    private val preferencesDataSource: CountryPreferencesDataSource
) : CountryRepository {

    override fun saveSelectedCountry(countryCode: String) {
        preferencesDataSource.saveCountry(countryCode)
    }

    override fun getSelectedCountry(): String {
        return preferencesDataSource.getCountry()
    }
}
