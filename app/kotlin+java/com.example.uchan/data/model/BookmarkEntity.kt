package com.example.uchan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val url: String,
    val boardId: String,
    val threadId: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis()
) 