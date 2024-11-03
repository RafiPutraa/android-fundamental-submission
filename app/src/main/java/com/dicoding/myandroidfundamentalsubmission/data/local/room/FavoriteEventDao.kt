package com.dicoding.myandroidfundamentalsubmission.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.myandroidfundamentalsubmission.data.local.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM fav_event ORDER BY id DESC")
    fun getEvent(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT * FROM fav_event where bookmarked = 1")
    fun getBookmarkedEvent(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT * FROM fav_event WHERE id = :id")
    fun getFavoriteEventById(id: Int): LiveData<FavoriteEventEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(favorite: List<FavoriteEventEntity>)

    @Update
    fun updateEvent(favorite: FavoriteEventEntity)

    @Query("DELETE FROM fav_event WHERE bookmarked = 0")
    fun delete()

    @Query("DELETE FROM fav_event WHERE id = :id")
    fun deleteFavoriteById(id: Int): Int

    @Query("SELECT EXISTS(SELECT * FROM fav_event WHERE id = :id AND bookmarked = 1)")
    fun isEventBookmarked(id: Int): Boolean
}