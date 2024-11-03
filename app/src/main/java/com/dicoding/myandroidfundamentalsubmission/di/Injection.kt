package com.dicoding.myandroidfundamentalsubmission.di

import android.content.Context
import com.dicoding.myandroidfundamentalsubmission.data.FavoriteEventRepository
import com.dicoding.myandroidfundamentalsubmission.data.local.room.FavoriteEventDatabase
import com.dicoding.myandroidfundamentalsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.myandroidfundamentalsubmission.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteEventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventDatabase.getInstance(context)
        val dao = database.favoriteEventDao()
        val appExecutors = AppExecutors()
        return FavoriteEventRepository.getInstance(apiService, dao, appExecutors)
    }
}