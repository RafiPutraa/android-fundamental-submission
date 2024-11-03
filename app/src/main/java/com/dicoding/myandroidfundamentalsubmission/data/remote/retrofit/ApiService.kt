package com.dicoding.myandroidfundamentalsubmission.data.remote.retrofit

import com.dicoding.myandroidfundamentalsubmission.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>
}