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

sealed class ArticlesUiState {
    object Loading : ArticlesUiState()
    data class Success(val articles: List<ArticleModel>) : ArticlesUiState()
    data class Error(val message: String) : ArticlesUiState()
}

class ArticlesViewModel(
    private val newsCallable: NewsCallable,
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _articlesState = MutableStateFlow<ArticlesUiState>(ArticlesUiState.Loading)
    val articlesState: StateFlow<ArticlesUiState> = _articlesState

    fun loadArticles(category: String) {
        _articlesState.value = ArticlesUiState.Loading

        // ✅ No need for viewModelScope.launch here
        viewModelScope.launch {
            val country = settingRepository.getSelectedCountry().first()

            // ⛔ Remove 'enqueue' from inside launch
            newsCallable.getNews(category, country)
                .enqueue(object : Callback<NewsModel> {
                    override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                        if (response.isSuccessful) {
                            val articles = response.body()?.articleModel?.filterNot {
                                it.title == "[Removed]"
                            } ?: emptyList()

                            android.util.Log.d("ArticlesViewModel", "Articles received: ${articles.size}")

                            _articlesState.value = ArticlesUiState.Success(articles)
                        } else {
                            android.util.Log.e("ArticlesViewModel", "Response failed: ${response.code()}")
                            _articlesState.value = ArticlesUiState.Error("Failed to load news")
                        }
                    }


                    override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                        _articlesState.value = ArticlesUiState.Error(t.message ?: "Unknown error")
                    }
                })
        }
    }
}
