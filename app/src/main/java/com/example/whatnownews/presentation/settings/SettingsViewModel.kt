package com.example.whatnownews.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.domain.repository.settings.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingRepository: SettingRepository
) : ViewModel() {

    val currentCountry: Flow<String> get() = settingRepository.getSelectedCountry()

    val isDarkMode: Flow<Boolean> get() = settingRepository.isDarkMode()

    fun saveCountry(it: String) {
        viewModelScope.launch {
            settingRepository.saveSelectedCountry(it)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            settingRepository.toggleDarkMode()
        }
    }
}