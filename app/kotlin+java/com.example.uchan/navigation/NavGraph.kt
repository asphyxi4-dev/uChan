package com.example.uchan.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Thread : Screen("thread/{boardId}") {
        fun createRoute(boardId: String) = "thread/$boardId"
    }
    object ThreadViewer : Screen("thread/{boardId}/{threadId}") {
        fun createRoute(boardId: String, threadId: String) = "thread/$boardId/$threadId"
    }
    object Bookmarks : Screen("bookmarks")
    object Settings : Screen("settings")

    companion object {
        val defaultBoards = listOf(
            "g",  // Technology
            "a",  // Anime & Manga
            "v",  // Video Games
            "pol", // Politically Incorrect
            "b"   // Random
        )

        private val _customBoards = mutableListOf<String>()
        val customBoards: List<String> get() = _customBoards

        fun addCustomBoard(boardId: String) {
            if (!_customBoards.contains(boardId) && !defaultBoards.contains(boardId)) {
                _customBoards.add(boardId)
            }
        }

        fun removeCustomBoard(boardId: String) {
            _customBoards.remove(boardId)
        }
    }
} 