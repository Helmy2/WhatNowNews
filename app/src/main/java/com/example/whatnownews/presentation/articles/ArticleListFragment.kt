package com.example.whatnownews.presentation.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.whatnownews.databinding.FragmentArticleListBinding
import com.example.whatnownews.domain.models.articles.ArticleModel
import com.example.whatnownews.domain.models.category.Category
import com.example.whatnownews.presentation.common.CATEGORY_KEY
import com.example.whatnownews.presentation.favorites.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleListFragment : Fragment() {

    private lateinit var binding: FragmentArticleListBinding
    private val category: String by lazy {
        arguments?.getString(CATEGORY_KEY) ?: Category.General.categoryName
    }

    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private val newsViewModel: NewsViewModel by viewModel()

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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            newsViewModel.uiState.collect { state ->
                when (state) {
                    is NewsUiState.Loading -> binding.progress.isVisible = true
                    is NewsUiState.Success -> {
                        binding.progress.isVisible = false
                        showNews(ArrayList(state.articles))
                    }
                    is NewsUiState.Error -> {
                        binding.progress.isVisible = false
                    }
                }
            }
        }

        newsViewModel.loadNews(category)

        binding.swipeRefresh.setOnRefreshListener {
            newsViewModel.loadNews(category)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showNews(articles: ArrayList<ArticleModel>) {
        val adapter = NewsAdapter(requireActivity(), articles, favoritesViewModel)
        binding.newsLis.adapter = adapter
    }
}
