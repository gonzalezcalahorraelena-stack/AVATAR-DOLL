package com.example.avatar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AvatarDao {
    @Query("SELECT * FROM saved_avatars ORDER BY timestamp DESC")
    fun getAllAvatars(): Flow<List<SavedAvatarEntity>>

    @Query("SELECT * FROM saved_avatars WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteAvatars(): Flow<List<SavedAvatarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvatar(avatar: SavedAvatarEntity): Long

    @Update
    suspend fun updateAvatar(avatar: SavedAvatarEntity)

    @Delete
    suspend fun deleteAvatar(avatar: SavedAvatarEntity)

    @Query("DELETE FROM saved_avatars WHERE id = :id")
    suspend fun deleteAvatarById(id: Long)
}
