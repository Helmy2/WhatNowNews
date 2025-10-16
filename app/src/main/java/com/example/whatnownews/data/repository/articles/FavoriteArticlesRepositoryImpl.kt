package com.example.whatnownews.data.repository.articles

import com.example.whatnownews.data.remote.favorites.FavoriteArticlesDS
import com.example.whatnownews.domain.models.articles.FavoriteArticlesModel
import com.example.whatnownews.domain.repository.articles.FavoriteArticlesRepository

class FavoriteArticlesRepositoryImpl(
    private val remote: FavoriteArticlesDS
) : FavoriteArticlesRepository {

    override suspend fun getArticles(): List<FavoriteArticlesModel> {
        return remote.getArticles()
    }

    override suspend fun updateFavorite(articleId: String, isFav: Boolean) {
        remote.updateFavorite(articleId, isFav)
    }

    override fun listenToFavorites(onResult: (List<FavoriteArticlesModel>) -> Unit) {
        remote.listenToFavoriteArticles(onResult)
    }


    override suspend fun addFavorite(article: FavoriteArticlesModel) {
        remote.addFavorite(article)
    }
}

