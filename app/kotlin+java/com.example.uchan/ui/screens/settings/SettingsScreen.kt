package com.example.uchan.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uchan.ui.theme.Theme
import com.example.uchan.ui.theme.Type.FontOption

// Welcome to the Settings Screen! This is where you can make the app truly yours ^^
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = viewModel()
) {
    // Keep track of our current theme and font :3
    val currentTheme by viewModel.currentTheme.collectAsState()
    val currentFont by viewModel.currentFont.collectAsState()
    
    // Remember if we're showing our cool dialog boxes ^^
    var showThemeDialog by remember { mutableStateOf(false) }
    var showFontDialog by remember { mutableStateOf(false) }

    // Theme picker dialog - so many pretty colors! :3
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Select Theme") },
            text = {
                Column {
                    // Show all our awesome themes~
                    Theme.values().forEach { theme ->
                        ListItem(
                            headlineContent = { Text(theme.displayName()) },
                            leadingContent = {
                                RadioButton(
                                    selected = theme == currentTheme,
                                    onClick = {
                                        viewModel.setTheme(theme)
                                        showThemeDialog = false
                                    }
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.setTheme(theme)
                                showThemeDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Font picker dialog - choose your style! ^^
    if (showFontDialog) {
        AlertDialog(
            onDismissRequest = { showFontDialog = false },
            title = { Text("Select Font") },
            text = {
                Column {
                    // Look at all these cool fonts :3
                    FontOption.values().forEach { font ->
                        ListItem(
                            headlineContent = { 
                                Text(
                                    font.displayName(),
                                    fontFamily = font.family
                                )
                            },
                            leadingContent = {
                                RadioButton(
                                    selected = font == currentFont,
                                    onClick = {
                                        viewModel.setFont(font)
                                        showFontDialog = false
                                    }
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.setFont(font)
                                showFontDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // The main settings screen layout ^^
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance section header~
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium
            )
            
            // Theme picker card - so colorful! :3
            OutlinedCard(
                modifier = Modifier.clickable { showThemeDialog = true }
            ) {
                ListItem(
                    headlineContent = { Text("Theme") },
                    supportingContent = { Text(currentTheme.displayName()) },
                    leadingContent = { 
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Theme"
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Select Theme"
                        )
                    }
                )
            }
            
            // Font picker card - express yourself! ^^
            OutlinedCard(
                modifier = Modifier.clickable { showFontDialog = true }
            ) {
                ListItem(
                    headlineContent = { Text("Font") },
                    supportingContent = { 
                        Text(
                            currentFont.displayName(),
                            fontFamily = currentFont.family
                        )
                    },
                    leadingContent = { 
                        Icon(
                            Icons.Default.TextFormat,
                            contentDescription = "Font"
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Select Font"
                        )
                    }
                )
            }
        }
    }
} 