package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val id: String, // use url as id
    val title: String,
    val url: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
