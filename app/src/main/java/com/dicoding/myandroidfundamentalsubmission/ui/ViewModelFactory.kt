package com.dicoding.myandroidfundamentalsubmission.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.myandroidfundamentalsubmission.data.FavoriteEventRepository
import com.dicoding.myandroidfundamentalsubmission.data.local.room.FavoriteEventDatabase
import com.dicoding.myandroidfundamentalsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.myandroidfundamentalsubmission.ui.favorite.FavoriteViewModel
import com.dicoding.myandroidfundamentalsubmission.ui.setting.SettingPreferences
import com.dicoding.myandroidfundamentalsubmission.utils.AppExecutors
import com.dicoding.myandroidfundamentalsubmission.ui.setting.dataStore

class ViewModelFactory(
    private val pref: SettingPreferences,
    private val favoriteEventRepository: FavoriteEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(favoriteEventRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: run {
                    val database = FavoriteEventDatabase.getInstance(context)
                    val repository = FavoriteEventRepository.getInstance(
                        apiService = ApiConfig.getApiService(),
                        favoriteEventDao = database.favoriteEventDao(),
                        appExecutors = AppExecutors()
                    )
                    val preferences = SettingPreferences.getInstance(context.dataStore)
                    ViewModelFactory(preferences, repository).also { instance = it }
                }
            }
        }
    }
}
