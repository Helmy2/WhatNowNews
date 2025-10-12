package com.example.whatnownews.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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

        binding.btnGoToLogin.setOnClickListener {
            // Check verification status before navigating
            viewModel.checkVerificationStatus()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.checkState.collectLatest { state ->
                state?.let {
                    when (it) {
                        is Resource.Success -> {
                            if (it.data == true) {
                                findNavController().navigate(R.id.action_emailVerificationFragment_to_loginFragment)
                            } else {
                                Toast.makeText(requireContext(), "Please verify your email first.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), it.message ?: "Error checking status", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            // No explicit progress UI here; could add if desired
                        }
                    }
                }
            }
        }
    }
}
