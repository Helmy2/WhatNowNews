package com.example.whatnownews.data.api

data class News(
//    val status: String,
//    val totalResults: Int,
    val articles: ArrayList<Article>
)

data class Article(
    val title: String,
    val url: String,
    val urlToImage: String
)