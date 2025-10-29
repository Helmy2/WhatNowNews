package com.example.whatnownews.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.whatnownews.R
import com.example.whatnownews.databinding.FragmentHomeBinding
import com.example.whatnownews.domain.model.Category
import com.example.whatnownews.presentation.common.CATEGORY_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivFav.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
        }

        binding.ivOptions.setOnClickListener { anchorView ->
            showPopupMenu(anchorView)
        }

        setupCategoryClickListeners()
    }

    private fun setupCategoryClickListeners() {
        val categoryMap = mapOf(
            binding.cardCategoryGeneral to Category.General,
            binding.cardCategoryEntertainment to Category.Entertainment,
            binding.cardCategorySports to Category.Sports,
            binding.cardCategoryBusiness to Category.Business,
            binding.cardCategoryHealth to Category.Health,
            binding.cardCategoryScience to Category.Science
        )

        categoryMap.forEach { (cardView, category) ->
            cardView.setOnClickListener {
                navigateToArticleList(category.categoryName)
            }
        }
    }

    private fun navigateToArticleList(category: String) {
        val bundle = bundleOf(CATEGORY_KEY to category)
        findNavController().navigate(R.id.action_homeFragment_to_articleListFragment, bundle)
    }

    private fun showPopupMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menuInflater.inflate(R.menu.home_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                    true
                }

                R.id.action_logout -> {
                    viewModel.logout()
                    findNavController().navigate(R.id.action_global_loginFragment)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
}
