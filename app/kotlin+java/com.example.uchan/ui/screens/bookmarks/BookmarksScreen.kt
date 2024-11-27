package com.example.uchan.ui.screens.bookmarks

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uchan.navigation.Screen
import com.example.uchan.data.model.BookmarkEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    navController: NavController,
    viewModel: BookmarksViewModel = viewModel()
) {
    var showSortMenu by remember { mutableStateOf(false) }
    val bookmarks by viewModel.bookmarks.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks") },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.List, "Sort")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("No bookmarks yet")
                    Text(
                        "Bookmarked threads will appear here",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = bookmarks,
                    key = { it.url }
                ) { bookmark ->
                    BookmarkItem(
                        bookmark = bookmark,
                        onBookmarkClick = {
                            navController.navigate(Screen.Thread.createRoute(bookmark.boardId))
                        },
                        onDeleteClick = {
                            viewModel.removeBookmark(bookmark)
                        }
                    )
                }
            }
        }

        if (showSortMenu) {
            AlertDialog(
                onDismissRequest = { showSortMenu = false },
                title = { Text("Sort Bookmarks") },
                text = {
                    Column {
                        SortOption(
                            text = "Newest First",
                            selected = sortOrder == BookmarksViewModel.SortOrder.NEWEST,
                            onClick = {
                                viewModel.setSortOrder(BookmarksViewModel.SortOrder.NEWEST)
                                showSortMenu = false
                            }
                        )
                        SortOption(
                            text = "Oldest First",
                            selected = sortOrder == BookmarksViewModel.SortOrder.OLDEST,
                            onClick = {
                                viewModel.setSortOrder(BookmarksViewModel.SortOrder.OLDEST)
                                showSortMenu = false
                            }
                        )
                        SortOption(
                            text = "By Board",
                            selected = sortOrder == BookmarksViewModel.SortOrder.BY_BOARD,
                            onClick = {
                                viewModel.setSortOrder(BookmarksViewModel.SortOrder.BY_BOARD)
                                showSortMenu = false
                            }
                        )
                    }
                },
                confirmButton = {}
            )
        }
    }
}

@Composable
private fun SortOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkItem(
    bookmark: BookmarkEntity,
    onBookmarkClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        onClick = onBookmarkClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = { 
                Text("/${bookmark.boardId}/") 
            },
            supportingContent = { 
                Text(bookmark.title) 
            },
            trailingContent = {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete bookmark"
                    )
                }
            }
        )
    }
} 