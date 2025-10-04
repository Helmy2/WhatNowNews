package com.example.whatnownews.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.whatnownews.databinding.FragmentEmailVerificationBinding

class EmailVerificationFragment : Fragment() {

    private lateinit var binding: FragmentEmailVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }
}
