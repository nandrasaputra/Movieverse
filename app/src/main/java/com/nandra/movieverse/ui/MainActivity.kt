package com.nandra.movieverse.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.nandra.movieverse.R
import com.nandra.movieverse.util.setVisibilityGone
import com.nandra.movieverse.util.setVisibilityVisible
import com.nandra.movieverse.util.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupBottomNavigationView()
        }

        checkApiKeys()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationView()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when(destination.id) {
            R.id.detailFragmentInHome, R.id.detailFragmentInFavorite, R.id.detailFragmentInDiscover, R.id.searchFragment -> {showBottomNavBar(false) }
            else -> {showBottomNavBar(true)}
        }
    }

    private fun setupBottomNavigationView() {
        val navGraphIds = listOf(R.navigation.home_nav,R.navigation.discover_nav, R.navigation.favorite_nav, R.navigation.about_nav)

        val controller = main_activity_bottom_navigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.main_activity_nav_host,
            intent = intent
        )

        controller.observe(this) {
            addOnDestinationChangedListener(it)
        }
    }

    private fun addOnDestinationChangedListener(navController: NavController) {
        navController.removeOnDestinationChangedListener(this)
        navController.addOnDestinationChangedListener(this)
    }

    private fun showBottomNavBar(show: Boolean) {
        if (show) {
            main_activity_bottom_navigation.setVisibilityVisible()
        } else {
            main_activity_bottom_navigation.setVisibilityGone()
        }
    }

    private fun checkApiKeys() {
        val youtubeApiKey = getString(R.string.GOOGLE_YOUTUBE_API)
        val tmdbApiKey = getString(R.string.TMDB_API_KEY)

        if (youtubeApiKey.isEmpty() || tmdbApiKey.isEmpty()) {
            Toast.makeText(this, "Some API Key Is Missing, Please Put It On api.properties", Toast.LENGTH_LONG).show()
        }
    }

}