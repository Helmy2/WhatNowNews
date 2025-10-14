package com.example.whatnownews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(binding.root)

        // TODO: Replace this with your actual authentication logic
/*        val isAuthenticated = false

        val navHostFragment =binding.navHostFragment.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if (isAuthenticated) {
            navGraph.setStartDestination(R.id.homeFragment)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
        }

        navController.graph = navGraph*/
        observeAuthStatus()
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.nav_graph) // Your navigation graph XML file

        // Set the determined fragment as the starting point
        graph.setStartDestination(destinationId)

        // Apply the graph to the NavController
        navController.graph = graph
    }
}
