package com.example.whatnownews.data.api

import com.example.whatnownews.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {

    @GET("/v2/top-headlines?pageSize=50")
    fun getNews(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<News>


}
