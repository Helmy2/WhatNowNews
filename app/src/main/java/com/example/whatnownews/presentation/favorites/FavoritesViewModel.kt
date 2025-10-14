package com.example.whatnownews.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.whatnownews.domain.model.FavoriteArticlesModel
import com.example.whatnownews.domain.repository.ArticleRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _articles = MutableLiveData<List<FavoriteArticlesModel>>()
    val articles: LiveData<List<FavoriteArticlesModel>> = _articles
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        startListeningToFavorites()
    }

    private fun startListeningToFavorites() {
        articleRepository.listenToFavorites { articles ->
            _articles.postValue(articles)
        }
    }


    fun getArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            val list = articleRepository.getArticles()
            _articles.value = list
            _isLoading.value = false
        }
    }

    fun toggleFavorite(article: FavoriteArticlesModel) {
        val updated = article.copy(isFavorite = !article.isFavorite)
        viewModelScope.launch {
            articleRepository.updateFavorite(updated.id, updated.isFavorite)
        }
    }


    fun addFavorite(article: FavoriteArticlesModel) {
        viewModelScope.launch {
            articleRepository.addFavorite(article)
        }
    }
}


