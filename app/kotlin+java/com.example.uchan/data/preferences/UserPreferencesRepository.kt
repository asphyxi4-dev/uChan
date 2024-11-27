package com.example.uchan.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Hey! This is where we save all your preferences :3
// Like your favorite theme and font choices ^^

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {
    // Keys for our settings - like labels on storage boxes ^^
    private val themeKey = stringPreferencesKey("theme")
    private val fontKey = stringPreferencesKey("font")

    // Watch for theme changes~
    val currentTheme: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[themeKey] ?: "light"  // Light theme by default :3
        }

    // Save the new theme when you pick one ^^
    suspend fun setTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme  // Keep it safe!
        }
    }

    // Watch for font changes too~
    val currentFont: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[fontKey] ?: "roboto"  // Roboto is our default font ^^
        }

    // Save your font choice :3
    suspend fun setFont(font: String) {
        context.dataStore.edit { preferences ->
            preferences[fontKey] = font  // Remember it forever~
        }
    }
} 