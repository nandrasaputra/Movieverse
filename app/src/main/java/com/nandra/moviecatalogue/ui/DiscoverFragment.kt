package com.nandra.moviecatalogue.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.adapter.DiscoverViewPagerPageAdapter
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : Fragment() {

    private lateinit var discoverViewPagerPageAdapter: DiscoverViewPagerPageAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var currentLanguage: String? = ""
    private lateinit var languageEnglishValue : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()

        discoverViewPagerPageAdapter = DiscoverViewPagerPageAdapter(
            childFragmentManager,
            discover_fragment_tab_layout.tabCount
        )
        discover_fragment_viewpager.adapter = discoverViewPagerPageAdapter

        discover_fragment_tab_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                discover_fragment_viewpager.currentItem = tab?.position!!
            }
        })
        discover_fragment_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(discover_fragment_tab_layout))
        setTabItemTitle(currentLanguage!!)
    }

    private fun setTabItemTitle(language: String) {
        if (language == languageEnglishValue) {
            discover_fragment_tab_layout.getTabAt(0)?.text = getString(R.string.main_tab_1_title_en)
            discover_fragment_tab_layout.getTabAt(1)?.text = getString(R.string.main_tab_2_title_en)
        } else {
            discover_fragment_tab_layout.getTabAt(0)?.text = getString(R.string.main_tab_1_title_id)
            discover_fragment_tab_layout.getTabAt(1)?.text = getString(R.string.main_tab_2_title_id)
        }
    }

    private fun prepareSharedPreferences() {
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(getString(R.string.preferences_language_key),
            getString(R.string.preferences_language_value_english))
    }
}