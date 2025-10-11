package com.example.whatnownews.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.whatnownews.R
import com.example.whatnownews.databinding.FragmentEmailVerificationBinding

class EmailVerificationFragment : Fragment() {

    private lateinit var binding: FragmentEmailVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoToLogin.setOnClickListener {
            // Navigate to the login screen, assuming you have this action in your nav graph
            findNavController().navigate(R.id.action_emailVerificationFragment_to_loginFragment)
        }
    }
}
