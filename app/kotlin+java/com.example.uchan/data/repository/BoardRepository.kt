package com.example.uchan.data.repository

import com.example.uchan.data.model.Board
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BoardRepository {
    fun getBoards(): Flow<List<Board>> = flow {
        // TODO: Implement actual API calls
        emit(emptyList())
    }
} 