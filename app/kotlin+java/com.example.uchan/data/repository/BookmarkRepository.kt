package com.example.uchan.data.repository

import com.example.uchan.data.dao.BookmarkDao
import com.example.uchan.data.model.BookmarkEntity
import kotlinx.coroutines.flow.Flow

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {
    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
    
    suspend fun addBookmark(bookmark: BookmarkEntity) = bookmarkDao.insertBookmark(bookmark)
    
    suspend fun removeBookmark(bookmark: BookmarkEntity) = bookmarkDao.deleteBookmark(bookmark)
    
    suspend fun isBookmarked(url: String): Boolean = bookmarkDao.isBookmarked(url)
    
    suspend fun removeBookmarkByUrl(url: String) = bookmarkDao.deleteBookmarkByUrl(url)
} 