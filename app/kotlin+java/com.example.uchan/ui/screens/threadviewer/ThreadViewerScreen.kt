package com.example.uchan.ui.screens.threadviewer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uchan.ui.components.PostItem
import com.example.uchan.ui.components.ImageViewerDialog
import com.example.uchan.ui.components.ReplyDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadViewerScreen(
    navController: NavController,
    threadId: String?,
    boardId: String?,
    viewModel: ThreadViewerViewModel = viewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    var showReplyDialog by remember { mutableStateOf(false) }
    var replyToThread by remember { mutableStateOf(true) }
    
    LaunchedEffect(threadId, boardId) {
        if (threadId != null && boardId != null) {
            viewModel.loadThread(boardId, threadId)
        }
    }

    selectedImageUrl?.let { url ->
        ImageViewerDialog(
            imageUrl = url,
            onDismiss = { selectedImageUrl = null }
        )
    }

    if (showReplyDialog) {
        ReplyDialog(
            isReplyToThread = replyToThread,
            onDismiss = { showReplyDialog = false },
            onSubmit = { name, comment ->
                if (threadId != null && boardId != null) {
                    viewModel.submitReply(name, comment, threadId, boardId)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thread #$threadId") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        replyToThread = true
                        showReplyDialog = true 
                    }) {
                        Icon(Icons.Default.Add, "Reply to Thread")
                    }
                    IconButton(onClick = { 
                        if (threadId != null && boardId != null) {
                            viewModel.loadThread(boardId, threadId)
                        }
                    }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
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
                        Button(onClick = { 
                            if (threadId != null && boardId != null) {
                                viewModel.loadThread(boardId, threadId)
                            }
                        }) {
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
                        PostItem(
                            post = post,
                            onImageClick = { url -> selectedImageUrl = url }
                        )
                    }
                }
            }
        }
    }
} 