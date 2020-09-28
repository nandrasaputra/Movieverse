package com.nandra.movieverse.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.FavoriteViewPagerAdapter
import com.nandra.movieverse.util.tabTitleProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewPagerAdapter: FavoriteViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewPagerAdapter = FavoriteViewPagerAdapter(
            this,
            favorite_fragment_tab_layout.tabCount
        )

        favorite_fragment_viewpager.adapter = favoriteViewPagerAdapter

        TabLayoutMediator(favorite_fragment_tab_layout, favorite_fragment_viewpager) { tab, position ->
            tab.text = tabTitleProvider(requireContext(), position)
        }.attach()
    }
}