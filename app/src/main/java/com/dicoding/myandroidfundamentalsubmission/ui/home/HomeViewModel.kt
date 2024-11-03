package com.dicoding.myandroidfundamentalsubmission.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myandroidfundamentalsubmission.data.remote.response.EventResponse
import com.dicoding.myandroidfundamentalsubmission.data.remote.response.ListEventsItem
import com.dicoding.myandroidfundamentalsubmission.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun findEvent() {
        _isLoading.value = true
        _errorMessage.value = null

        val finishedClient = ApiConfig.getApiService().getEvents(0)
        finishedClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                Log.d("API Response", response.toString())
                if (response.isSuccessful) {
                    response.body()?.listEvents?.let { events ->
                        _finishedEvents.value = events
                    } ?: run {
                        _errorMessage.value = "No finished events found"
                        Log.e(TAG, "onResponse: Response body is null")
                    }
                }
                upcomingEvents()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value =
                    "Internet connection is not available. Please try again later."
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun upcomingEvents() {
        val upcomingClient = ApiConfig.getApiService().getEvents(1)
        upcomingClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.listEvents?.let { events ->
                        _upcomingEvents.value = events
                    }
                        ?: run {
                            _errorMessage.value = "No upcoming events found"
                            Log.e(TAG, "onResponse: Response body is null")
                        }
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