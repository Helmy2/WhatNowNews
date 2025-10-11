package com.example.whatnownews.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
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
            Toast.makeText(context, "Favourite Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.ivOptions.setOnClickListener { anchorView ->
            showPopupMenu(anchorView)
        }

        binding.cardCategory1.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_articleListFragment)
        }
    }

    private fun showPopupMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menuInflater.inflate(R.menu.home_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show()
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
