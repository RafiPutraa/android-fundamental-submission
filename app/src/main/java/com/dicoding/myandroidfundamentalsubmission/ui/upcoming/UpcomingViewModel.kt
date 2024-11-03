package com.dicoding.myandroidfundamentalsubmission.ui.upcoming

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.myandroidfundamentalsubmission.data.remote.response.EventResponse
import com.dicoding.myandroidfundamentalsubmission.data.remote.response.ListEventsItem
import com.dicoding.myandroidfundamentalsubmission.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    companion object {
        private const val TAG = "UpcomingViewModel"
    }

    private val _listEventsItem = MutableLiveData<List<ListEventsItem>>()
    val listEventsItem: LiveData<List<ListEventsItem>> = _listEventsItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun findEvent() {
        _isLoading.value = true
        _errorMessage.value = null

        val client = ApiConfig.getApiService().getEvents(1)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                Log.d("API Response", response.toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listEventsItem.value = response.body()?.listEvents
                    } else {
                        _errorMessage.value = "No events found"
                        Log.e(TAG, "onResponse: Response body is null")
                    }
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Internet connection is not available. Please try again later."
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
}