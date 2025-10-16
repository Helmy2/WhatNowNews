package com.example.whatnownews.presentation.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatnownews.data.remote.news.NewsCallable
import com.example.whatnownews.domain.models.articles.ArticleModel
import com.example.whatnownews.domain.models.news.NewsModel
import com.example.whatnownews.domain.repository.settings.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<ArticleModel>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(
    private val newsCallable: NewsCallable,
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState

    fun loadNews(category: String) {
        _uiState.value = NewsUiState.Loading

        viewModelScope.launch {
            val country = settingRepository.getSelectedCountry().first()
            newsCallable.getNews(category, country)
                .enqueue(object : Callback<NewsModel> {
                    override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                        if (response.isSuccessful) {
                            val articles = response.body()?.articles.orEmpty()
                                .filterNot { it.title == "[Removed]" }
                            _uiState.value = NewsUiState.Success(articles)
                        } else {
                            _uiState.value = NewsUiState.Error("Failed to load news")
                        }
                    }

                    override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                        _uiState.value = NewsUiState.Error(t.message ?: "Unknown error")
                    }
                })
        }
    }
}
