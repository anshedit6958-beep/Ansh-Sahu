package com.example.data

import kotlinx.coroutines.flow.Flow

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {
    val allBookmarks: Flow<List<Bookmark>> = bookmarkDao.getAllBookmarks()

    suspend fun insert(bookmark: Bookmark) = bookmarkDao.insertBookmark(bookmark)

    suspend fun deleteById(id: String) = bookmarkDao.deleteBookmarkById(id)
    
    fun isBookmarked(id: String): Flow<Boolean> = bookmarkDao.isBookmarked(id)
}
