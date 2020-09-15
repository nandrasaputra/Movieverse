package com.nandra.movieverse.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nandra.movieverse.ui.favoritemovie.FavoriteMovieFragment
import com.nandra.movieverse.ui.favoritetv.FavoriteTVFragment

class FavoriteViewPagerAdapter(
    fragment: Fragment,
    private val numberOfTab: Int
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteMovieFragment()
            1 -> FavoriteTVFragment()
            else -> throw Exception("Position: $position not available")
        }
    }

    override fun getItemCount(): Int {
        return numberOfTab
    }
}
