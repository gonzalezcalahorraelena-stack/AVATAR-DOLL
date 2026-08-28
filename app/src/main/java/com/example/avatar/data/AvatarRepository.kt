package com.example.avatar.data

import kotlinx.coroutines.flow.Flow

class AvatarRepository(private val avatarDao: AvatarDao) {
    val allAvatars: Flow<List<SavedAvatarEntity>> = avatarDao.getAllAvatars()
    val favoriteAvatars: Flow<List<SavedAvatarEntity>> = avatarDao.getFavoriteAvatars()

    suspend fun saveAvatar(avatar: SavedAvatarEntity): Long {
        return avatarDao.insertAvatar(avatar)
    }

    suspend fun updateAvatar(avatar: SavedAvatarEntity) {
        avatarDao.updateAvatar(avatar)
    }

    suspend fun deleteAvatar(avatar: SavedAvatarEntity) {
        avatarDao.deleteAvatar(avatar)
    }

    suspend fun deleteAvatarById(id: Long) {
        avatarDao.deleteAvatarById(id)
    }
}
