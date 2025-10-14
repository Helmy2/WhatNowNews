package com.example.whatnownews.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.whatnownews.databinding.FragmentForgotPasswordBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ForgotPasswordFragment : Fragment() {

    // 1. Koin Injection
    private val viewModel: ForgotPasswordViewModel by viewModel()

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeUiState()
        binding.btnSendResetLink.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            viewModel.sendResetLink(email)
        }
        observeUiState()
        binding.tvBackToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupListeners() {
        binding.btnSendResetLink.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            viewModel.sendResetLink(email)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ForgotPasswordUiState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSendResetLink.isEnabled = true
                        }
                        is ForgotPasswordUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnSendResetLink.isEnabled = false
                        }
                        is ForgotPasswordUiState.Success -> {
                            Toast.makeText(context, "Password reset link sent!", Toast.LENGTH_LONG).show()
                            // Navigate the user back to the login screen
                            // findNavController().popBackStack()
                        }
                        is ForgotPasswordUiState.Error -> {
                            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

