package com.example.whatnownews.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.whatnownews.R
import com.example.whatnownews.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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
            binding.cardCategoryGeneral to "general",
            binding.cardCategoryEntertainment to "entertainment",
            binding.cardCategorySports to "sports",
            binding.cardCategoryBusiness to "business",
            binding.cardCategoryHealth to "health",
            binding.cardCategoryScience to "science"
        )

        categoryMap.forEach { (cardView, category) ->
            cardView.setOnClickListener {
                navigateToArticleList(category)
            }
        }
    }

    private fun navigateToArticleList(category: String) {
        val bundle = bundleOf("category" to category)
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
                    Toast.makeText(context, "Logout Clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
}
