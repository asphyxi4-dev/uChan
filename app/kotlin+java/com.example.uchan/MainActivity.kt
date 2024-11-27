package com.example.uchan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uchan.navigation.Screen
import com.example.uchan.ui.components.BottomBar
import com.example.uchan.ui.screens.bookmarks.BookmarksScreen
import com.example.uchan.ui.screens.home.HomeScreen
import com.example.uchan.ui.screens.settings.SettingsScreen
import com.example.uchan.ui.screens.thread.ThreadScreen
import com.example.uchan.ui.theme.UChanTheme
import com.example.uchan.ui.screens.settings.SettingsViewModel
import com.example.uchan.ui.screens.threadviewer.ThreadViewerScreen
import com.example.uchan.ui.theme.Theme
import com.example.uchan.ui.theme.getTypography

// Hey there! This is where our app starts its journey! ^^
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Make it look nice on modern phones :3
        setContent {
            // Get our settings ready~
            val settingsViewModel: SettingsViewModel = viewModel()
            val currentTheme by settingsViewModel.currentTheme.collectAsStateWithLifecycle()
            val currentFont by settingsViewModel.currentFont.collectAsStateWithLifecycle()
            
            // Wrap everything in our beautiful theme! ^^
            UChanTheme(
                theme = currentTheme,
                typography = getTypography(currentFont.family)
            ) {
                // Navigation setup - helps us move between screens :3
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                
                // Show bottom bar only on main screens ^^
                val showBottomBar = when (navBackStackEntry?.destination?.route) {
                    Screen.Home.route, Screen.Bookmarks.route, Screen.Settings.route -> true
                    else -> false
                }
                
                // Our app's main layout~
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { 
                        BottomBar(
                            navController = navController,
                            visible = showBottomBar
                        )
                    }
                ) { paddingValues ->
                    // Navigation routes - where should we go? :3
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController = navController)  // Home sweet home ^^
                        }
                        composable(Screen.Thread.route) { backStackEntry ->
                            val boardId = backStackEntry.arguments?.getString("boardId")
                            ThreadScreen(navController = navController, boardId = boardId)
                        }
                        composable(Screen.Bookmarks.route) {
                            BookmarksScreen(navController = navController)  // Your favorite threads~
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController = navController)  // Make it yours! :3
                        }
                        composable(Screen.ThreadViewer.route) { backStackEntry ->
                            val boardId = backStackEntry.arguments?.getString("boardId")
                            val threadId = backStackEntry.arguments?.getString("threadId")
                            ThreadViewerScreen(
                                navController = navController,
                                boardId = boardId,
                                threadId = threadId
                            )
                        }
                    }
                }
            }
        }
    }
}