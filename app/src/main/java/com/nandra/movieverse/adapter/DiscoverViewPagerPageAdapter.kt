package com.nandra.movieverse.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nandra.movieverse.ui.DiscoverMovieFragment
import com.nandra.movieverse.ui.DiscoverTVFragment

class DiscoverViewPagerPageAdapter(
    fragmentManager: FragmentManager,
    private val numberOfTab: Int
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DiscoverMovieFragment()
            1 -> DiscoverTVFragment()
            else -> throw Exception()          //Throw Exception
        }
    }

    override fun getCount(): Int {
        return numberOfTab
    }
}