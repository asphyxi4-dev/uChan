package com.example.uchan.data.repository

import com.example.uchan.data.dao.ThreadDao
import com.example.uchan.data.model.ThreadEntity
import kotlinx.coroutines.flow.Flow

class ThreadRepository(private val threadDao: ThreadDao) {
    fun getThreadsByBoard(boardId: String): Flow<List<ThreadEntity>> = 
        threadDao.getThreadsByBoard(boardId)
    
    suspend fun getThread(threadId: String): ThreadEntity? = 
        threadDao.getThread(threadId)
    
    suspend fun saveThread(thread: ThreadEntity) = 
        threadDao.insertThread(thread)
    
    suspend fun deleteThread(thread: ThreadEntity) = 
        threadDao.deleteThread(thread)
    
    suspend fun clearBoardThreads(boardId: String) = 
        threadDao.deleteThreadsByBoard(boardId)
    
    suspend fun updateThreadPosition(threadId: String, position: Int) {
        threadDao.getThread(threadId)?.let { thread ->
            threadDao.insertThread(thread.copy(lastPosition = position))
        }
    }
} 