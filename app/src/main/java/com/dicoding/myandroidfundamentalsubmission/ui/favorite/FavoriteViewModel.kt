package com.dicoding.myandroidfundamentalsubmission.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myandroidfundamentalsubmission.data.FavoriteEventRepository
import com.dicoding.myandroidfundamentalsubmission.data.local.entity.FavoriteEventEntity
import com.dicoding.myandroidfundamentalsubmission.data.Result

class FavoriteViewModel(private val favoriteEventRepository: FavoriteEventRepository): ViewModel() {
    fun getAllEvent(): LiveData<Result<List<FavoriteEventEntity>>> {
        return favoriteEventRepository.getBookmarkedEvent()
    }

    fun getFavoriteEventById(id: Int) = favoriteEventRepository.getFavoriteEventById(id)

    fun saveEvent(favorite: FavoriteEventEntity, id: Int) = favoriteEventRepository.setBookmarkedEvent(favorite, true, id)

    fun deleteEvent(favorite: FavoriteEventEntity, id: Int) = favoriteEventRepository.setBookmarkedEvent(favorite, false, id)

}