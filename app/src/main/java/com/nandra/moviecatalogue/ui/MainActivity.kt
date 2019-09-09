package com.nandra.moviecatalogue.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.util.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentNavController: LiveData<NavController>? = null
    private var currentLanguage: String? = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var languageEnglishValue : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        prepareSharedPreferences()
        setBottomNavigationLabel(currentLanguage!!)
    }

    private fun setBottomNavigationLabel(language: String) {
        val menu = main_activity_bottom_navigation.menu
        if (language == languageEnglishValue) {
            menu[0].title = getString(R.string.title_discover_en)
            menu[1].title = getString(R.string.title_favorite_en)
            menu[2].title = getString(R.string.title_setting_en)
        } else {
            menu[0].title = getString(R.string.title_discover_id)
            menu[1].title = getString(R.string.title_favorite_id)
            menu[2].title = getString(R.string.title_setting_id)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_activity_bottom_navigation)

        val navGraphIds = listOf(R.navigation.discover_nav, R.navigation.favorite_nav, R.navigation.settings_nav)

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

}
