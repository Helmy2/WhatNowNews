package com.example.whatnownews.presentation.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.whatnownews.data.api.Article
import com.example.whatnownews.data.api.News
import com.example.whatnownews.data.api.NewsCallable
import com.example.whatnownews.databinding.FragmentArticleListBinding
import com.example.whatnownews.domain.model.Category
import com.example.whatnownews.presentation.common.CATEGORY_KEY
import com.example.whatnownews.presentation.favorites.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticleListFragment : Fragment() {

    private lateinit var binding: FragmentArticleListBinding
    private val category: String by lazy {
        arguments?.getString(CATEGORY_KEY) ?: Category.General.categoryName
    }

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = category.replaceFirstChar { it.uppercase() }
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        loadNews()

        binding.swipeRefresh.setOnRefreshListener {
            loadNews()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun loadNews() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val c = retrofit.create(NewsCallable::class.java)
        c.getNews(category).enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    response.body()?.articles?.let { articles ->
                        articles.removeAll { it.title == "[Removed]" }
                        showNews(articles)
                    }
                }
                binding.progress.isVisible = false
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                binding.progress.isVisible = false
            }
        })
    }

    private fun showNews(articles: ArrayList<Article>) {
        val adapter = NewsAdapter(requireActivity(), articles, favoritesViewModel)
        binding.newsLis.adapter = adapter
    }
}
