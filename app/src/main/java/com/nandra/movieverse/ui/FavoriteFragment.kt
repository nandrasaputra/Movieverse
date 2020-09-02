package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.FavoriteViewPagerPageAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewPagerAdapter: FavoriteViewPagerPageAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()

        favoriteViewPagerAdapter = FavoriteViewPagerPageAdapter(
            childFragmentManager,
            favorite_fragment_tab_layout.tabCount
        )
        favorite_fragment_viewpager.adapter = favoriteViewPagerAdapter
        favorite_fragment_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                favorite_fragment_viewpager.currentItem = tab?.position!!
            }
        })
        favorite_fragment_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(favorite_fragment_tab_layout))
        setTabItemTitle()
    }

    private fun setTabItemTitle() {
        favorite_fragment_tab_layout.getTabAt(0)?.text = getString(R.string.main_tab_1_title_en)
        favorite_fragment_tab_layout.getTabAt(1)?.text = getString(R.string.main_tab_2_title_en)
    }

    private fun prepareSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }


}