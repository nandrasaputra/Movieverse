package com.nandra.movieverse.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.endiar.movieverse.core.utils.Constant
import com.google.android.material.tabs.TabLayoutMediator
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverViewPagerAdapter
import com.nandra.movieverse.util.tabTitleProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_discover.*

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private lateinit var discoverViewPagerAdapter: DiscoverViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

        discoverViewPagerAdapter = DiscoverViewPagerAdapter(this, discover_fragment_tab_layout.tabCount)

        discover_fragment_viewpager.adapter = discoverViewPagerAdapter

        TabLayoutMediator(discover_fragment_tab_layout, discover_fragment_viewpager) { tab, position ->
            tab.text = tabTitleProvider(requireContext(), position)
        }.attach()

        discover_fragment_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position) {
                    0 -> {
                        discover_searchview.queryHint = "Search Movie"
                    }
                    1 -> {
                        discover_searchview.queryHint = "Search TV"
                    }
                    else -> throw Exception("Invalid Position")
                }
            }
        })

        discover_image_search_dummy.setOnClickListener {
            val action = when(discover_fragment_tab_layout.selectedTabPosition) {
                0 -> { DiscoverFragmentDirections.actionDiscoverFragmentToSearchFragment(Constant.MOVIE_FILM_TYPE) }
                1 -> {DiscoverFragmentDirections.actionDiscoverFragmentToSearchFragment(Constant.TV_FILM_TYPE) }
                else -> {throw Exception("Invalid Position")}
            }

            findNavController().navigate(action)
        }
    }
}