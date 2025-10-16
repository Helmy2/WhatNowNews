package com.example.whatnownews.domain.repository.articles

import com.example.whatnownews.domain.models.articles.FavoriteArticlesModel

interface FavoriteArticlesRepository {
    suspend fun getArticles(): List<FavoriteArticlesModel>
    suspend fun updateFavorite(articleId: String, isFav: Boolean)

    fun listenToFavorites(onResult: (List<FavoriteArticlesModel>) -> Unit)

    suspend fun addFavorite(article: FavoriteArticlesModel)
}

