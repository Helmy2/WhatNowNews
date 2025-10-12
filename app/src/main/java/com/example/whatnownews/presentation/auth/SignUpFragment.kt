package com.example.whatnownews.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.whatnownews.R
import com.example.whatnownews.databinding.FragmentSignUpBinding
import com.example.whatnownews.core.util.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listener for the sign-up button
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            viewModel.signUp(email, password, confirmPassword)
        }

        // Set up click listener for the login text
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        // Observe the sign-up state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpState.collectLatest { resource ->
                // The initial state is null, so we only react to non-null values.
                resource?.let {
                    when (it) {
                        is Resource.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is Resource.Success -> {
                            binding.progressBar.isVisible = false
                            Toast.makeText(requireContext(), "Verification email sent!", Toast.LENGTH_SHORT).show()
                            // Navigate to the email verification screen
                            findNavController().navigate(R.id.action_signUpFragment_to_emailVerificationFragment)
                        }
                        is Resource.Error -> {
                            binding.progressBar.isVisible = false
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
