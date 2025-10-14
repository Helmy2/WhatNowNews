package com.example.whatnownews.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.whatnownews.R
import com.example.whatnownews.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    // Koin injects the ViewModel, scoped to this Fragment
    private val viewModel: LoginViewModel by viewModel()

    // View Binding setup for Fragments
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginClickListener()
        observeLoginState()
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_signUpFragment
            )
        }
        binding.tvForgotPassword.setOnClickListener {
         findNavController().navigate(
             R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun loginClickListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }
    }

    private fun observeLoginState() {
        // Use viewLifecycleOwner.lifecycleScope to ensure the coroutine is cancelled
        // when the Fragment's view is destroyed.
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                // Hide all UI elements initially and show them based on state
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true

                when (state) {
                    is LoginUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnLogin.isEnabled = false
                    }

                    is LoginUiState.Success -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        // Navigate to the home/main screen using Navigation Component
                        // val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        // findNavController().navigate(action)
                        findNavController().navigate(
                            R.id.action_loginFragment_to_homeFragment, null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.nav_graph, true) // pop everything up to root
                                .build()
                        )
                    }

                    is LoginUiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }

                    is LoginUiState.Idle -> {
                        // Do nothing, initial state
                    }
                }
            }
        }
    }

    // Crucial for preventing memory leaks with View Binding in Fragments
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
