package com.example.whatnownews.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {

    @GET("/v2/top-headlines?country=us&apiKey=0583e213bf1c43e0869b848320f15e70&pageSize=50")
    fun getNews(@Query("category") category: String): Call<News>

}
