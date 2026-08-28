package com.example.avatar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_avatars")
data class SavedAvatarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val artStyle: String,
    val gender: String,
    val configJson: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
