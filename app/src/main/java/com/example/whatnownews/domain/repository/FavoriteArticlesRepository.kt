package com.example.whatnownews.domain.repository

import com.example.whatnownews.domain.model.FavoriteArticlesModel

interface ArticleRepository {
    suspend fun getArticles(): List<FavoriteArticlesModel>
    suspend fun updateFavorite(articleId: String, isFav: Boolean)

     fun listenToFavorites(onResult: (List<FavoriteArticlesModel>) -> Unit)

    suspend fun addFavorite(article: FavoriteArticlesModel)
}

