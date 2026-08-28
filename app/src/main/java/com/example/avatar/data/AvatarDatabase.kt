package com.example.avatar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedAvatarEntity::class], version = 1, exportSchema = false)
abstract class AvatarDatabase : RoomDatabase() {
    abstract fun avatarDao(): AvatarDao

    companion object {
        @Volatile
        private var INSTANCE: AvatarDatabase? = null

        fun getDatabase(context: Context): AvatarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AvatarDatabase::class.java,
                    "avatar_maestro_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
