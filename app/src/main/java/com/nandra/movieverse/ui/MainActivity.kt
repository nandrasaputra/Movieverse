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
import com.nandra.movieverse.util.setupWithNavController
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentNavController: LiveData<NavController>? = null
    private var currentLanguage: String? = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var languageEnglishValue : String
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        prepareSharedPreferences()
        setBottomNavigationLabel(currentLanguage!!)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        viewModel.isOnDetailFragment.observe(this, Observer {
            handleBottomNavigationAppearance(it)
        })
    }

    private fun setBottomNavigationLabel(language: String) {
        val menu = main_activity_bottom_navigation.menu
        if (language == languageEnglishValue) {
            menu[0].title = getString(R.string.title_home_en)
            menu[1].title = getString(R.string.title_discover_en)
            menu[2].title = getString(R.string.title_favorite_en)
            menu[3].title = getString(R.string.title_setting_en)
        } else {
            menu[0].title = getString(R.string.title_home_id)
            menu[1].title = getString(R.string.title_discover_id)
            menu[2].title = getString(R.string.title_favorite_id)
            menu[3].title = getString(R.string.title_setting_id)
        }
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

        /*controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })*/
        currentNavController = controller
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)
        setBottomNavigationLabel(currentLanguage!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun prepareSharedPreferences() {
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        currentLanguage = sharedPreferences.getString(getString(R.string.preferences_language_key),
            getString(R.string.preferences_language_value_english))
    }

    private fun handleBottomNavigationAppearance(value: Boolean) {
        if(value) {
            main_activity_bottom_navigation.visibility = View.GONE
        } else {
            main_activity_bottom_navigation.visibility = View.VISIBLE
        }
    }

}
