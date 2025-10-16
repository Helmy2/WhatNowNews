package com.example.whatnownews.domain.repository

interface CountryRepository {
    fun saveSelectedCountry(countryCode: String)
    fun getSelectedCountry(): String
}