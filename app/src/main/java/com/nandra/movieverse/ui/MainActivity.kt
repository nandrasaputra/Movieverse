package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nandra.movieverse.R
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.util.setupWithNavController
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        prepareSharedPreferences()
        setBottomNavigationLabel()
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        viewModel.isOnDetailFragment.observe(this) {
            handleBottomNavigationAppearance(it)
        }
    }

    private fun setBottomNavigationLabel() {
        val menu = main_activity_bottom_navigation.menu
        menu[0].title = getString(R.string.title_home_en)
        menu[1].title = getString(R.string.title_discover_en)
        menu[2].title = getString(R.string.title_favorite_en)
        menu[3].title = getString(R.string.title_setting_en)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_activity_bottom_navigation)

        val navGraphIds = listOf(R.navigation.home_nav, R.navigation.discover_nav, R.navigation.favorite_nav, R.navigation.settings_nav)

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.main_fragment_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            Constant.PREFERENCE_KEY_TODAY_RELEASES -> {
            }
            Constant.PREFERENCE_KEY_REMINDER -> {
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun prepareSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun handleBottomNavigationAppearance(value: Boolean) {
        if(value) {
            main_activity_bottom_navigation.visibility = View.GONE
        } else {
            main_activity_bottom_navigation.visibility = View.VISIBLE
        }
    }

}
