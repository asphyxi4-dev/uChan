package com.example.uchan.ui.components

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoardItem(
    boardId: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteButton by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { 
                    Log.d("BoardItem", "Long press detected for board: $boardId")
                    showDeleteButton = true 
                }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "/$boardId/",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )
            
            // Delete button overlay
            if (showDeleteButton) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .combinedClickable(
                            onClick = { showDeleteButton = false },
                            onLongClick = {}
                        )
                )
                
                IconButton(
                    onClick = {
                        Log.d("BoardItem", "Delete clicked for board: $boardId")
                        onDelete()
                        showDeleteButton = false
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove board",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
} 