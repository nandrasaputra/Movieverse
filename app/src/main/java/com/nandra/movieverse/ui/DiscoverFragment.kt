package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverViewPagerPageAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_discover.*


class DiscoverFragment : Fragment() {

    private lateinit var discoverViewPagerPageAdapter: DiscoverViewPagerPageAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var currentLanguage: String? = ""
    private lateinit var languageEnglishValue : String
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
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
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                adjustQueryHintText(currentLanguage!!, tab?.position!!)
                discover_fragment_viewpager.currentItem = tab.position
            }
        })
        discover_fragment_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(discover_fragment_tab_layout))
        discover_image_search_dummy.setOnClickListener {
            val tabPosition = discover_fragment_tab_layout.selectedTabPosition
            val type : String = if (tabPosition == 0) {
                Constant.MOVIE_FILM_TYPE
            } else {
                Constant.TV_FILM_TYPE
            }
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToSearchFragment(type)
            findNavController().navigate(action)
        }
        setTabItemTitle(currentLanguage!!)
        adjustQueryHintText(currentLanguage!!, discover_fragment_tab_layout.selectedTabPosition)
        sharedViewModel.retryLoadAllFailed()
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

    private fun adjustQueryHintText(language: String, tabPosition: Int) {
        if (tabPosition == 0) {
            if (language == languageEnglishValue) {
                discover_searchview.queryHint = getString(R.string.movie_search_view_query_hint_en)
            } else {
                discover_searchview.queryHint = getString(R.string.movie_search_view_query_hint_id)
            }
        } else {
            if (language == languageEnglishValue) {
                discover_searchview.queryHint = getString(R.string.tv_search_view_query_hint_en)
            } else {
                discover_searchview.queryHint = getString(R.string.tv_search_view_query_hint_id)
            }
        }
    }

    private fun prepareSharedPreferences() {
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(getString(R.string.preferences_language_key),
            getString(R.string.preferences_language_value_english))
    }
}