package com.example.whatnownews.data.preferences

interface CountryPreferencesDataSource {
    fun saveCountry(countryCode: String)
    fun getCountry(): String
}