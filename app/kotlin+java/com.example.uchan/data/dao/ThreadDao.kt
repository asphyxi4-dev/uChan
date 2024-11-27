package com.example.uchan.data.dao

import androidx.room.*
import com.example.uchan.data.model.ThreadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreadDao {
    @Query("SELECT * FROM threads WHERE boardId = :boardId ORDER BY lastUpdated DESC")
    fun getThreadsByBoard(boardId: String): Flow<List<ThreadEntity>>
    
    @Query("SELECT * FROM threads WHERE threadId = :threadId")
    suspend fun getThread(threadId: String): ThreadEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(thread: ThreadEntity)
    
    @Delete
    suspend fun deleteThread(thread: ThreadEntity)
    
    @Query("DELETE FROM threads WHERE boardId = :boardId")
    suspend fun deleteThreadsByBoard(boardId: String)
} 