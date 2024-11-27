package com.example.uchan.data.model

data class Post(
    val postId: String,
    val author: String,
    val timestamp: String,
    val content: String,
    val imageUrl: String? = null,
    val thumbnailUrl: String? = null,
    val replies: List<String> = emptyList(),
    val isThread: Boolean = false,
    val boardId: String = ""
) 