package com.example.whatnownews.domain.usecase.sharedprefs
import com.example.whatnownews.domain.repository.CountryRepository

class SaveSelectedCountryUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(countryCode: String) = repository.saveSelectedCountry(countryCode)
}
