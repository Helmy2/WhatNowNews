package com.example.whatnownews.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.whatnownews.databinding.FragmentSettingsBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private var isProgrammaticChange = false
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.currentCountry.first().let {
                when (it) {
                    "us" -> binding.countriesRadioGroup.check(binding.unitedStatesRadioButton.id)
                    "eg" -> binding.countriesRadioGroup.check(binding.egyptRadioButton.id)
                    "de" -> binding.countriesRadioGroup.check(binding.germanyRadioButton.id)
                }
            }
            viewModel.isDarkMode.first().let {
                binding.isDarkModeswitch.isChecked = it
                isProgrammaticChange = true
            }

            binding.countriesRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selected = when (checkedId) {
                    binding.unitedStatesRadioButton.id -> "us"
                    binding.egyptRadioButton.id -> "eg"
                    binding.germanyRadioButton.id -> "de"
                    else -> null
                }
                selected?.let { viewModel.saveCountry(it) }
            }
            binding.isDarkModeswitch.setOnCheckedChangeListener { _, isChecked ->
                if (isProgrammaticChange) {
                    viewModel.toggleDarkMode()
                }
            }
        }
    }
}

