package com.example.uchan.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchan.data.db.UChanDatabase
import com.example.uchan.data.preferences.UserPreferencesRepository
import com.example.uchan.ui.theme.Theme
import com.example.uchan.ui.theme.Type.FontOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Hey there! This is our settings brain - it remembers all your preferences! ^^
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    // Get our database and preferences ready~
    private val database = UChanDatabase.getInstance(application)
    private val userPreferencesRepository = UserPreferencesRepository(application)
    
    // Keep track of database clearing status :3
    private val _isDatabaseClearing = MutableStateFlow(false)
    val isDatabaseClearing = _isDatabaseClearing.asStateFlow()
    
    // Remember which theme we're using ^^
    private val _currentTheme = MutableStateFlow(Theme.LIGHT)
    val currentTheme = _currentTheme.asStateFlow()
    
    // And which font family is active~
    private val _currentFont = MutableStateFlow(FontOption.ROBOTO)
    val currentFont = _currentFont.asStateFlow()
    
    init {
        // Watch for theme changes in a coroutine :3
        viewModelScope.launch {
            userPreferencesRepository.currentTheme.collect { themeName ->
                _currentTheme.value = when (themeName) {
                    "dark" -> Theme.DARK           // For night owls ^^
                    "vaporwave" -> Theme.VAPORWAVE // A E S T H E T I C
                    "tech" -> Theme.TECH           // Cyberpunk vibes~
                    "4chan" -> Theme.FOURCHAN      // Classic style
                    "fallout" -> Theme.FALLOUT     // Post-apocalyptic mood
                    else -> Theme.LIGHT            // Default and clean :3
                }
            }
        }

        // Watch for font changes too! ^^
        viewModelScope.launch {
            userPreferencesRepository.currentFont.collect { fontName ->
                _currentFont.value = when (fontName.lowercase()) {
                    "ubuntu" -> FontOption.UBUNTU     // Round and friendly~
                    "firacode" -> FontOption.FIRA_CODE // For the coders ^^
                    "comicneue" -> FontOption.COMIC_NEUE // Fun and casual
                    "dos" -> FontOption.DOS           // Retro vibes :3
                    else -> FontOption.ROBOTO         // Clean default
                }
            }
        }
    }
    
    // Save the new theme when user picks one ^^
    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            userPreferencesRepository.setTheme(
                when (theme) {
                    Theme.DARK -> "dark"
                    Theme.VAPORWAVE -> "vaporwave" // So aesthetic~
                    Theme.TECH -> "tech"
                    Theme.FOURCHAN -> "4chan"
                    Theme.FALLOUT -> "fallout"
                    Theme.LIGHT -> "light"
                }
            )
        }
    }
    
    // Save the new font when user changes it :3
    fun setFont(font: FontOption) {
        viewModelScope.launch {
            userPreferencesRepository.setFont(
                when (font) {
                    FontOption.UBUNTU -> "ubuntu"
                    FontOption.FIRA_CODE -> "firacode"
                    FontOption.COMIC_NEUE -> "comicneue"
                    FontOption.DOS -> "dos"         // Retro style ^^
                    FontOption.ROBOTO -> "roboto"
                }
            )
        }
    }
    
    // Clean up everything if needed~
    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isDatabaseClearing.value = true
                database.clearAllTables()  // Spring cleaning! ^^
            } finally {
                _isDatabaseClearing.value = false
            }
        }
    }
} 