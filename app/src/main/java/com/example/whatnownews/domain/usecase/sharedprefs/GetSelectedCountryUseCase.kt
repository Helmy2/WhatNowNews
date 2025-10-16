package com.example.whatnownews.domain.usecase.sharedprefs
import com.example.whatnownews.domain.repository.CountryRepository

class GetSelectedCountryUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): String = repository.getSelectedCountry()
}
