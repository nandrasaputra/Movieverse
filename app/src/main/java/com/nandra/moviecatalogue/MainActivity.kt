package com.nandra.moviecatalogue

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.nandra.moviecatalogue.adapter.ViewPagerPageAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPagerPageAdapter: ViewPagerPageAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var currentLanguage: String? = ""
    private lateinit var languageEnglishValue : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareSharedPreferences()

        tabLayout = findViewById(R.id.main_tab_layout)

        viewPagerPageAdapter = ViewPagerPageAdapter(
            supportFragmentManager,
            tabLayout.tabCount
        )
        main_viewpager.adapter = viewPagerPageAdapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_viewpager.currentItem = tab?.position!!
            }
        })
        main_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        setTabItemTitle(currentLanguage!!)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)
        setTabItemTitle(currentLanguage!!)
    }

    private fun setTabItemTitle(language: String) {
        if (language == languageEnglishValue) {
            tabLayout.getTabAt(0)?.text = getString(R.string.main_tab_1_title_en)
            tabLayout.getTabAt(1)?.text = getString(R.string.main_tab_2_title_en)
        } else {
            tabLayout.getTabAt(0)?.text = getString(R.string.main_tab_1_title_id)
            tabLayout.getTabAt(1)?.text = getString(R.string.main_tab_2_title_id)
        }
    }

    private fun prepareSharedPreferences() {
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        currentLanguage = sharedPreferences.getString(getString(R.string.preferences_language_key),
            getString(R.string.preferences_language_value_english))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.main_menu_action_settings) {
            val intent = Intent(this, SettingActivity::class.java)
            this.startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic val EXTRA_MOVIE = "extra_movie"
    }
}
