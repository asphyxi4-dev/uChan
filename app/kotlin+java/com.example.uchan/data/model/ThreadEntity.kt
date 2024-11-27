package com.example.uchan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "threads")
data class ThreadEntity(
    @PrimaryKey
    val threadId: String,
    val boardId: String,
    val title: String,
    val lastUpdated: Long = System.currentTimeMillis(),
    val lastPosition: Int = 0, // For saving scroll position
    val isWatched: Boolean = false
) 