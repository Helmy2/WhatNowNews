package com.example.whatnownews.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.whatnownews.databinding.FragmentSettingsBinding
import com.example.whatnownews.domain.usecase.sharedprefs.GetSelectedCountryUseCase
import com.example.whatnownews.domain.usecase.sharedprefs.SaveSelectedCountryUseCase
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val getCountry: GetSelectedCountryUseCase by inject()
    private val saveCountry: SaveSelectedCountryUseCase by inject()

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

        val savedCountry = getCountry()

        when (savedCountry) {
            "us" -> binding.countriesRadioGroup.check(binding.unitedStatesRadioButton.id)
            "eg" -> binding.countriesRadioGroup.check(binding.egyptRadioButton.id)
            "de" -> binding.countriesRadioGroup.check(binding.germanyRadioButton.id)
        }

        binding.countriesRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selected = when (checkedId) {
                binding.unitedStatesRadioButton.id -> "us"
                binding.egyptRadioButton.id -> "eg"
                binding.germanyRadioButton.id -> "de"
                else -> null
            }
            selected?.let { saveCountry(it) }
        }
    }
}
