package com.example.repository

import com.example.db.BookmarkDao
import com.example.model.BookmarkEntity
import kotlinx.coroutines.flow.Flow

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {
    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    suspend fun insert(bookmark: BookmarkEntity) = bookmarkDao.insertBookmark(bookmark)

    suspend fun deleteById(id: String) = bookmarkDao.deleteBookmarkById(id)

    fun isBookmarkedFlow(id: String): Flow<Boolean> = bookmarkDao.isBookmarkedFlow(id)

    suspend fun isBookmarked(id: String): Boolean = bookmarkDao.isBookmarked(id)
}
