package com.example.whatnownews.domain.model

data class FavoriteArticlesModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    var isFavorite: Boolean = true
)
