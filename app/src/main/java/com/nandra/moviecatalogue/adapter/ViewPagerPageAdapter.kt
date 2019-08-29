package com.nandra.moviecatalogue.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nandra.moviecatalogue.ui.MovieFragment
import com.nandra.moviecatalogue.ui.TvShowFragment

class ViewPagerPageAdapter(
    fragmentManager: FragmentManager,
    private val numberOfTab: Int
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MovieFragment()
            1 -> TvShowFragment()
            else -> throw Exception()          //Throw Exception
        }
    }

    override fun getCount(): Int {
        return numberOfTab
    }
}