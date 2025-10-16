package com.example.whatnownews.data.preferences

import android.content.SharedPreferences

class CountryPreferencesDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : CountryPreferencesDataSource {

    override fun saveCountry(countryCode: String) {
        sharedPreferences.edit().putString("selectedCountry", countryCode).apply()
    }

    override fun getCountry(): String {
        return sharedPreferences.getString("selectedCountry", "us") ?: "us"
    }
}