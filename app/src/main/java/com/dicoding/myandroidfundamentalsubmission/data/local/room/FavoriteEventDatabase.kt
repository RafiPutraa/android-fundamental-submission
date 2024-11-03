package com.dicoding.myandroidfundamentalsubmission.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.myandroidfundamentalsubmission.data.local.entity.FavoriteEventEntity

@Database(entities = [FavoriteEventEntity::class], version = 1, exportSchema = false)
abstract class FavoriteEventDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteEventDatabase? = null

        @JvmStatic
        private var instance: FavoriteEventDatabase? = null
        fun getInstance(context: Context): FavoriteEventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteEventDatabase::class.java, "favorite_event.db"
                ).build()
            }
    }

}