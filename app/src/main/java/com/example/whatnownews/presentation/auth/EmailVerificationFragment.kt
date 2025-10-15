package com.example.whatnownews.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.whatnownews.R
import com.example.whatnownews.core.util.Resource
import com.example.whatnownews.databinding.FragmentEmailVerificationBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmailVerificationFragment : Fragment() {

    private lateinit var binding: FragmentEmailVerificationBinding
    private val viewModel: EmailVerificationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display user's email
        val userEmail = viewModel.getUserEmail()
        if (userEmail != null) {
            binding.tvUserEmail.text = userEmail
        }

        // Disable back button to prevent returning to verification screen
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing - back button is disabled
                Toast.makeText(
                    requireContext(),
                    "Please verify your email or use a different email to continue",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        setupClickListeners()
        observeStates()
    }

    private fun setupClickListeners() {
        // Check verification status button
        binding.btnCheckVerification.setOnClickListener {
            viewModel.checkVerificationStatus()
        }

        // Resend verification email button
        binding.btnResendVerification.setOnClickListener {
            viewModel.resendVerification()
        }

        // Use different email button - sign out and navigate back to sign up
        binding.btnUseDifferentEmail.setOnClickListener {
            // Sign out the unverified user before going back to sign up
            viewModel.signOutUnverifiedUser()
            findNavController().navigate(
                R.id.signUpFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, false) // Clear back to login, signup will be on top
                    .build()
            )
        }
    }

    private fun observeStates() {
        // Observe verification check state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.checkState.collectLatest { state ->
                state?.let {
                    when (it) {
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            if (it.data == true) {
                                // Email is verified - mark it and navigate to home
                                isEmailVerifiedAndNavigated = true
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.email_verified_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Navigate to home page and clear entire back stack
                                findNavController().navigate(
                                    R.id.action_emailVerificationFragment_to_homeFragment,
                                    null,
                                    NavOptions.Builder()
                                        .setPopUpTo(R.id.loginFragment, true)
                                        .build()
                                )
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.email_not_verified),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                it.message ?: "Error checking status",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        // Observe resend state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resendState.collectLatest { state ->
                state?.let {
                    when (it) {
                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.verification_email_sent),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.message ?: "Error sending email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Loading -> {
                        }
                    }
                }
            }
        }

        // Observe timer
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timerSeconds.collectLatest { seconds ->
                if (seconds > 0) {
                    binding.btnResendVerification.text = getString(R.string.resend_in, seconds)
                } else {
                    binding.btnResendVerification.text = getString(R.string.resend_verification_email)
                }
            }
        }

        // Observe resend button enabled state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isResendEnabled.collectLatest { enabled ->
                binding.btnResendVerification.isEnabled = enabled
            }
        }
    }

    private var isEmailVerifiedAndNavigated = false

    override fun onDestroyView() {
        super.onDestroyView()
        if (!isEmailVerifiedAndNavigated) {
            viewModel.signOutUnverifiedUser()
        }
    }
}
