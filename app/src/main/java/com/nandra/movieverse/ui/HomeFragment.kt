package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.NowPlayingAdapter
import com.nandra.movieverse.adapter.TrendingCardAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.listTrendingLive.observe(this, Observer {
            home_trending_card_slider.adapter = TrendingCardAdapter(it)
        })
        sharedViewModel.listNowPlayingLive.observe(this, Observer {
            home_now_playing_card_slider.adapter = NowPlayingAdapter(it)
        })
        sharedViewModel.isHomeError.observe(this, Observer {
            handleError(it)
        })
        sharedViewModel.isHomeLoading.observe(this, Observer {
            checkLoadingState(it)
        })
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
        prepareSharedPreferences()
        attemptPrepareView()
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
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
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        if (sharedViewModel.isHomeDataHasLoaded){
            home_trending_card_slider.adapter = TrendingCardAdapter(sharedViewModel.listTrendingLive.value!!)
            home_now_playing_card_slider.adapter = NowPlayingAdapter(sharedViewModel.listNowPlayingLive.value!!)
        }
        else {
            scope.launch {
                sharedViewModel.requestHomeData()
            }
        }
    }

    private fun checkLoadingState(state: Boolean) {
        if (state) {
            home_error_back.visibility = View.GONE
            home_progress_bar.visibility = View.VISIBLE
        } else {
            home_progress_bar.visibility = View.GONE
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
        if (currentLanguage == languageEnglishValue)
            home_error_button.text = getString(R.string.button_try_again_en)
        else
            home_error_button.text = getString(R.string.button_try_again_id)
    }
}