package com.nandra.movieverse.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nandra.movieverse.ui.discovermovie.DiscoverMovieFragment
import com.nandra.movieverse.ui.discovertv.DiscoverTVFragment

class DiscoverViewPagerAdapter(
    fragment: Fragment,
    private val numberOfTab: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return numberOfTab
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DiscoverMovieFragment()
            1 -> DiscoverTVFragment()
            else -> throw Exception("Position: $position is not available!")
        }
    }
}
