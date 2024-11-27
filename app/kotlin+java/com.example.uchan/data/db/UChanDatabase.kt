package com.example.uchan.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uchan.data.dao.BookmarkDao
import com.example.uchan.data.model.BookmarkEntity

@Database(
    entities = [
        BookmarkEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class UChanDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    
    companion object {
        @Volatile
        private var INSTANCE: UChanDatabase? = null
        
        fun getInstance(context: Context): UChanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UChanDatabase::class.java,
                    "uchan_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 