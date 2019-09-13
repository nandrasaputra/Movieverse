package com.nandra.moviecatalogue.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nandra.moviecatalogue.ui.FavoriteMovieFragment
import com.nandra.moviecatalogue.ui.FavoriteTVFragment

class FavoriteViewPagerPageAdapter(
    fragmentManager: FragmentManager,
    private val numberOfTab: Int
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteMovieFragment()
            1 -> FavoriteTVFragment()
            else -> throw Exception()          //Throw Exception
        }
    }

    override fun getCount(): Int {
        return numberOfTab
    }
}