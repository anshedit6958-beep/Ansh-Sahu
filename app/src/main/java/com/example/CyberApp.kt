package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.BookmarkRepository

class CyberApp : Application() {
    val database by lazy { Room.databaseBuilder(this, AppDatabase::class.java, "cyber_database").build() }
    val repository by lazy { BookmarkRepository(database.bookmarkDao()) }
}
