package com.example.learncodeapp.screens

import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    var isDarkTheme: Boolean = false
    var selectedLanguage: String = "Русский"

    fun updateTheme(isDark: Boolean) {
        isDarkTheme = isDark
    }

    fun updateLanguage(language: String) {
        selectedLanguage = language
    }
}
