package com.dicoding.myandroidfundamentalsubmission.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.myandroidfundamentalsubmission.data.local.entity.FavoriteEventEntity
import com.dicoding.myandroidfundamentalsubmission.data.local.room.FavoriteEventDao
import com.dicoding.myandroidfundamentalsubmission.data.remote.retrofit.ApiService
import com.dicoding.myandroidfundamentalsubmission.utils.AppExecutors

class FavoriteEventRepository private constructor(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao,
    private val appExecutors: AppExecutors
) {
    fun getFavoriteEventById(id: Int): LiveData<FavoriteEventEntity> {
        return favoriteEventDao.getFavoriteEventById(id)
    }

    fun getBookmarkedEvent(): LiveData<Result<List<FavoriteEventEntity>>> {
        val result = MediatorLiveData<Result<List<FavoriteEventEntity>>>()
        result.value = Result.Loading

        val localData = favoriteEventDao.getEvent()
        result.addSource(localData) { favoriteEvents ->
            if (favoriteEvents.isNotEmpty()) {
                result.value = Result.Success(favoriteEvents)
            } else {
                result.value = Result.Error("No favorite events found")
            }
        }

        return result
    }

    fun setBookmarkedEvent(event: FavoriteEventEntity, bookmarkState: Boolean, id: Int) {
        appExecutors.diskIO.execute {
            event.isBookmarked = bookmarkState
            Log.d("FavoriteEventRepository", "Setting bookmark state to $bookmarkState for event: $event")
            if (bookmarkState) {
                favoriteEventDao.insertEvent(listOf(event))
            } else {
                favoriteEventDao.deleteFavoriteById(id)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: FavoriteEventRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteEventDao: FavoriteEventDao,
            appExecutors: AppExecutors
        ): FavoriteEventRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteEventRepository(apiService, favoriteEventDao, appExecutors)
            }.also { instance = it }
    }
}