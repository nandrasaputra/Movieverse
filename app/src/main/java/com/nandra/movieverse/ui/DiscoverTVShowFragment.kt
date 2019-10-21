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
import androidx.recyclerview.widget.GridLayoutManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverPagedListAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_discover_tv.*

class DiscoverTVShowFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val discoverTVAdapter = DiscoverPagedListAdapter(Constant.TV_FILM_TYPE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover_tv, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.isLoading.observe(this, Observer {
            checkLoadingState(it)
        })
        sharedViewModel.isError.observe(this, Observer {
            errorIndicator(it)
        })
        /*sharedViewModel.listTVLive.observe(this, Observer {
            discover_tv_recyclerview.swapAdapter(DiscoverRecyclerViewAdapter(it, Constant.TV_FILM_TYPE), true)
        })*/
        sharedViewModel.getDiscoverTVData().observe(this, Observer {
            discoverTVAdapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        discover_tv_recyclerview.apply {
            /*hasFixedSize()*/
            layoutManager = GridLayoutManager(context, 3)
        }
        attemptPrepareView()
    }

    private fun checkLoadingState(state: Boolean) {
        if (state) {
            discover_tv_error_back.visibility = View.GONE
            discover_tv_progress_bar.visibility = View.VISIBLE
        }
        else {
            discover_tv_progress_bar.visibility = View.GONE
        }
    }

    private fun errorIndicator(state: Boolean){
        if(state){
            discover_tv_error_back.visibility = View.VISIBLE
            viewErrorLanguageAdjustment()
            discover_tv_error_button.setOnClickListener {
                prepareTVShowListView()
            }
        } else {
            discover_tv_error_back.visibility = View.GONE
        }
    }

    private fun attemptPrepareView() {
        if(sharedViewModel.isError.value == true) {
            errorIndicator(sharedViewModel.isError.value!!)
            return
        }
        prepareTVShowListView()
    }

    private fun prepareTVShowListView() {
        /*val loadingState = sharedViewModel.isLoading.value
        loadingState?.let { checkLoadingState(loadingState) }
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        if (sharedViewModel.isDataHasLoaded)
            discover_tv_recyclerview.swapAdapter(DiscoverRecyclerViewAdapter(sharedViewModel.listTVLive.value!!, Constant.TV_FILM_TYPE), true)
        else {
            scope.launch {
                sharedViewModel.requestDiscoverData()
            }
        }*/
        discover_tv_recyclerview.adapter = discoverTVAdapter
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }

    private fun viewErrorLanguageAdjustment() {
        if (currentLanguage == languageEnglishValue) {
            discover_tv_error_button.text = getString(R.string.button_try_again_en)
            discover_tv_error_text.text = getString(R.string.no_internet_connection_en)
        }
        else {
            discover_tv_error_button.text = getString(R.string.button_try_again_id)
            discover_tv_error_text.text = getString(R.string.no_internet_connection_id)
        }
    }
}