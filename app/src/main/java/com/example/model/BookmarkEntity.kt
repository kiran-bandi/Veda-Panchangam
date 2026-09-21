package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val type: String, // "DATE" or "FESTIVAL"
    val title: String,
    val subtitle: String,
    val rawDate: String, // YYYY-MM-DD
    val icon: String, // Emoji or symbol
    val timestamp: Long = System.currentTimeMillis()
)
