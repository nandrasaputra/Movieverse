package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.NowPlayingRecyclerViewAdapter
import com.nandra.movieverse.adapter.TrendingCardAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.listTrendingLive.observe(this) {
            home_trending_card_slider.adapter = TrendingCardAdapter(it)
        }
        sharedViewModel.listNowPlayingLive.observe(this) {
            home_now_playing_card_slider.adapter = NowPlayingRecyclerViewAdapter(it)
        }
        sharedViewModel.isHomeError.observe(this) {
            handleError(it)
        }
        sharedViewModel.isHomeLoading.observe(this) {
            checkLoadingState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        home_cover.setOnClickListener {  }  //Prevent Click Through
        prepareSharedPreferences()
        attemptPrepareView()
        setupNowPlaying()
        languageAdjustment()
    }

    private fun prepareSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private fun attemptPrepareView() {
        val loadingState = sharedViewModel.isHomeLoading.value
        loadingState?.let { checkLoadingState(loadingState) }
        if(sharedViewModel.isHomeError.value == true) {
            handleError(sharedViewModel.isHomeError.value!!)
        }
        prepareHomeView()
    }

    private fun prepareHomeView() {
        if (sharedViewModel.isHomeDataHasLoaded){
            home_trending_card_slider.adapter = TrendingCardAdapter(sharedViewModel.listTrendingLive.value!!)
            home_now_playing_card_slider.adapter = NowPlayingRecyclerViewAdapter(sharedViewModel.listNowPlayingLive.value!!)
        }
        else {
            viewLifecycleOwner.lifecycleScope.launch {
                sharedViewModel.requestHomeData()
            }
        }
    }

    private fun checkLoadingState(state: Boolean) {
        if (state) {
            home_error_back.visibility = View.GONE
            home_cover.visibility = View.VISIBLE
            home_shimmer.visibility = View.VISIBLE
            home_shimmer.startShimmer()
        } else {
            home_shimmer.stopShimmer()
            home_shimmer.visibility = View.GONE
            home_cover.visibility = View.GONE
        }
    }

    private fun handleError(state: Boolean) {
        if (state) {
            home_error_back.visibility = View.VISIBLE
            viewLanguageAdjustment()
            home_error_button.setOnClickListener {
                prepareHomeView()
            }
        } else {
            home_error_back.visibility = View.GONE
        }
    }

    private fun viewLanguageAdjustment() {
        home_error_button.text = getString(R.string.button_try_again_en)
        home_error_text.text = getString(R.string.no_internet_connection_en)
    }
    
    private fun setupNowPlaying() {
        home_now_playing_card_slider.setItemTransformer(ScaleTransformer.Builder().setMinScale(0.8F).build())
    }

    private fun languageAdjustment() {
        home_now_playing_text.text = resources.getString(R.string.home_now_playing_en)
        home_now_trending_text.text = resources.getString(R.string.home_trending_en)
    }
}