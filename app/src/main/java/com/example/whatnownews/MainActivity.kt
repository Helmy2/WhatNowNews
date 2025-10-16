package com.example.whatnownews

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.whatnownews.databinding.ActivityMainBinding
import com.example.whatnownews.presentation.auth.AuthStatus
import com.example.whatnownews.presentation.auth.SplashViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val splashViewModel: SplashViewModel by viewModel()
    private var isNavigationDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        handleDarkMode()
        setContentView(binding.root)
        handleWindowInsets()
        observeAuthStatus()
    }

    private fun handleDarkMode() {
        lifecycleScope.launch {
            splashViewModel.isDarkMode.collectLatest {
                AppCompatDelegate.setDefaultNightMode(
                    if (it) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                )
            }
        }
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.root.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            v.setBackgroundColor(Color.BLACK)
            insets
        }
    }

    private fun observeAuthStatus() {
        lifecycleScope.launch {

            // Use collectLatest to ensure we only act on the final state (Authenticated or Unauthenticated)
            splashViewModel.authStatus.collectLatest { status ->
                if (isNavigationDone) return@collectLatest // Already routed

                when (status) {
                    AuthStatus.Loading -> {
                        // Optionally show a lightweight progress view over the NavHost
                    }

                    AuthStatus.Authenticated -> {
                        navigateToDestination(R.id.homeFragment)
                        isNavigationDone = true
                    }

                    AuthStatus.Unauthenticated -> {
                        navigateToDestination(R.id.loginFragment)
                        isNavigationDone = true
                    }
                }
            }
        }
    }

    private fun navigateToDestination(destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val graph =
            navController.navInflater.inflate(R.navigation.nav_graph) // Your navigation graph XML file

        // Set the determined fragment as the starting point
        graph.setStartDestination(destinationId)

        // Apply the graph to the NavController
        navController.graph = graph
    }
}
