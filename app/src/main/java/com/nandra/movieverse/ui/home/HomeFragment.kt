package com.nandra.movieverse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.FilmGist
import com.google.android.material.snackbar.Snackbar
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.NowPlayingRecyclerViewAdapter
import com.nandra.movieverse.adapter.TrendingCardAdapter
import com.nandra.movieverse.util.setVisibilityGone
import com.nandra.movieverse.util.setVisibilityVisible
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_now_playing_content.*
import kotlinx.android.synthetic.main.home_now_trending_content.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeTrendingCardAdapter: TrendingCardAdapter
    private lateinit var nowPlayingRecyclerViewAdapter: NowPlayingRecyclerViewAdapter
    private var snackBar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    override fun onPause() {
        super.onPause()
        snackBar?.dismiss()
    }

    private fun setupView() {
        homeTrendingCardAdapter = TrendingCardAdapter(arrayListOf())
        home_now_playing_card_slider.setItemTransformer(ScaleTransformer.Builder().setMinScale(0.8F).build())
        nowPlayingRecyclerViewAdapter = NowPlayingRecyclerViewAdapter(listOf())
        home_now_playing_card_slider.adapter = nowPlayingRecyclerViewAdapter
        home_trending_card_slider.adapter = homeTrendingCardAdapter
    }

    private fun observeData() {
        homeViewModel.nowPlayingData.observe(viewLifecycleOwner) {resource ->
            resource?.let {
                when(it) {
                    is Resource.Loading -> {
                        fragment_home_now_trending_layout.setVisibilityGone()
                        fragment_home_now_trending_shimmer_layout.setVisibilityVisible()
                    }
                    is Resource.Success -> {
                        nowPlayingRecyclerViewAdapter.submitList(resource.data)
                        fragment_home_now_trending_layout.setVisibilityVisible()
                        fragment_home_now_trending_shimmer_layout.setVisibilityGone()
                    }
                    is Resource.Error -> {
                        fragment_home_now_trending_layout.setVisibilityGone()
                        fragment_home_now_trending_shimmer_layout.setVisibilityVisible()
                        showErrorSnackBar()
                    }
                }
            }
        }

        homeViewModel.trendingData.observe(viewLifecycleOwner) {resource ->
            resource?.let {
                when(it) {
                    is Resource.Loading -> {
                        fragment_home_now_playing_layout.setVisibilityGone()
                        fragment_home_now_playing_shimmer_layout.setVisibilityVisible()
                    }
                    is Resource.Success -> {
                        home_trending_card_slider.adapter = TrendingCardAdapter(resource.data as ArrayList<FilmGist>)
                        fragment_home_now_playing_layout.setVisibilityVisible()
                        fragment_home_now_playing_shimmer_layout.setVisibilityGone()

                    }
                    is Resource.Error -> {
                        fragment_home_now_playing_layout.setVisibilityGone()
                        fragment_home_now_playing_shimmer_layout.setVisibilityVisible()
                        showErrorSnackBar()
                    }
                }
            }
        }
    }

    private fun showErrorSnackBar() {
        snackBar = Snackbar.make(
            requireView(),
            "Error When Loading Data",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Retry") {
            homeViewModel.retryAllFailed()
        }.setAnchorView(fragment_home_snackbar_anchor)
        snackBar?.show()
    }
}