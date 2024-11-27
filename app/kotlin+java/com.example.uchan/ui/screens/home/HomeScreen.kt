package com.example.uchan.ui.screens.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchan.data.db.UChanDatabase
import com.example.uchan.data.model.BookmarkEntity
import com.example.uchan.data.repository.BookmarkRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookmarkRepository
    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST)
    val sortOrder = _sortOrder.asStateFlow()

    init {
        val database = UChanDatabase.getInstance(application)
        repository = BookmarkRepository(database.bookmarkDao())
    }

    val bookmarks: StateFlow<List<BookmarkEntity>> = combine(
        repository.getAllBookmarks(),
        _sortOrder
    ) { bookmarks, sortOrder ->
        when (sortOrder) {
            SortOrder.NEWEST -> bookmarks.sortedByDescending { it.timestamp }
            SortOrder.OLDEST -> bookmarks.sortedBy { it.timestamp }
            SortOrder.BY_BOARD -> bookmarks.sortedBy { it.boardId }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun removeBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.removeBookmark(bookmark)
        }
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    enum class SortOrder {
        NEWEST,
        OLDEST,
        BY_BOARD
    }
} 