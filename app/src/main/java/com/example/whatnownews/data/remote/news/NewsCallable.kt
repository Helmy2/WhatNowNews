package com.example.whatnownews.data.remote.news

import com.example.whatnownews.BuildConfig
import com.example.whatnownews.domain.models.news.NewsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {

    @GET("/v2/top-headlines?pageSize=50")
    fun getNews(
        @Query("category") category: String,
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<NewsModel>


}