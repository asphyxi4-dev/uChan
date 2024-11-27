package com.example.uchan.ui.screens.thread

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uchan.navigation.Screen
import com.example.uchan.ui.components.PostItem
import com.example.uchan.ui.components.ImageViewerDialog
import com.example.uchan.ui.components.ThreadItem
import com.example.uchan.ui.components.NewThreadDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadScreen(
    navController: NavController,
    boardId: String?,
    viewModel: ThreadViewModel = viewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    var showNewThreadDialog by remember { mutableStateOf(false) }
    val isBookmarked by viewModel.isBookmarked.collectAsState()
    
    LaunchedEffect(boardId) {
        boardId?.let { viewModel.loadThread(it) }
    }

    selectedImageUrl?.let { url ->
        ImageViewerDialog(
            imageUrl = url,
            onDismiss = { selectedImageUrl = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("/$boardId/") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadThread(boardId ?: "") }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewThreadDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, "New Thread")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(error ?: "Unknown error")
                        Button(onClick = { viewModel.loadThread(boardId ?: "") }) {
                            Text("Retry")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(posts) { post ->
                        ThreadItem(
                            thread = post,
                            onImageClick = { url -> selectedImageUrl = url },
                            onClick = { 
                                boardId?.let { bid ->
                                    navController.navigate(Screen.ThreadViewer.createRoute(bid, post.postId))
                                }
                            },
                            isBookmarked = isBookmarked[post.postId] ?: false,
                            onBookmarkClick = {
                                boardId?.let { bid ->
                                    viewModel.toggleBookmark(post, bid)
                                }
                            }
                        )
                    }
                }
            }
        }

        if (showNewThreadDialog) {
            NewThreadDialog(
                boardId = boardId ?: "",
                onDismiss = { showNewThreadDialog = false },
                onSubmit = { name, subject, comment, captchaResponse ->
                    viewModel.createThread(
                        boardId = boardId ?: "",
                        name = name,
                        subject = subject,
                        comment = comment,
                        captchaResponse = captchaResponse
                    )
                }
            )
        }
    }
} 