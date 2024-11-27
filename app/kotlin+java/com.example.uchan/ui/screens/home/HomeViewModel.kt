package com.example.uchan.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.uchan.data.repository.BoardRepository
import com.example.uchan.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val repository: BoardRepository = BoardRepository()
) : ViewModel() {
    private val defaultBoards = listOf("g", "a", "v", "pol", "b")
    private val _boards = MutableStateFlow(defaultBoards)
    val boards = _boards.asStateFlow()

    fun addBoard(boardId: String) {
        val currentBoards = _boards.value.toMutableList()
        if (!currentBoards.contains(boardId)) {
            currentBoards.add(boardId)
            _boards.value = currentBoards
            Log.d("HomeViewModel", "Board added: $boardId, Current boards: ${_boards.value}")
        }
    }

    fun removeBoard(boardId: String) {
        Log.d("HomeViewModel", "Attempting to remove board: $boardId")
        val currentBoards = _boards.value.toMutableList()
        if (currentBoards.remove(boardId)) {
            _boards.value = currentBoards
            Log.d("HomeViewModel", "Board removed: $boardId, Remaining boards: ${_boards.value}")
        } else {
            Log.d("HomeViewModel", "Failed to remove board: $boardId, Board not found in list")
        }
    }

    init {
        Log.d("HomeViewModel", "Initialized with boards: ${_boards.value}")
    }
} 