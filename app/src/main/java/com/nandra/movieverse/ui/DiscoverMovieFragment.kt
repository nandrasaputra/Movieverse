package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverPagedListAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.util.NetworkState
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_discover_movie.*

class DiscoverMovieFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val discoverMovieAdapter = DiscoverPagedListAdapter(Constant.MOVIE_FILM_TYPE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover_movie, container, false)
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
        sharedViewModel.movieDiscoverPagingList.observe(this, Observer {
            discoverMovieAdapter.submitList(it)
        })
        sharedViewModel.networkState.observe(this, Observer {
            handleNetworkState(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        movie_recyclerview.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
        movie_recyclerview.adapter = discoverMovieAdapter
    }

    private fun checkLoadingState(state: Boolean) {
        if (state) {
            movie_error_back.visibility = View.GONE
            discover_movie_progress_bar.visibility = View.VISIBLE
        }
        else {
            discover_movie_progress_bar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    private fun errorIndicator(state: Boolean){
        if(state){
            movie_error_back.visibility = View.VISIBLE
            viewErrorLanguageAdjustment()
            movie_error_button.setOnClickListener {
                prepareMovieListView()
            }
        } else {
            movie_error_back.visibility = View.GONE
        }
    }

    private fun attemptPrepareView() {
        val loadingState = sharedViewModel.isLoading.value
        loadingState?.let { checkLoadingState(loadingState) }
        if(sharedViewModel.isError.value == true) {
            errorIndicator(sharedViewModel.isError.value!!)
        }
        prepareMovieListView()
    }

    private fun prepareMovieListView() {
        /*val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        if (sharedViewModel.isDataHasLoaded)
            movie_recyclerview.swapAdapter(DiscoverRecyclerViewAdapter(sharedViewModel.listMovieLive.value!!, Constant.MOVIE_FILM_TYPE), true)
        else {
            scope.launch {
                sharedViewModel.requestDiscoverData()
            }
        }*/

    }

    private fun handleNetworkState(state: NetworkState) {
        when(state) {
            NetworkState.LOADING -> {discover_movie_progress_bar.visibility = View.VISIBLE}
            NetworkState.LOADED -> {discover_movie_progress_bar.visibility = View.GONE}
            NetworkState.error("No Internet Connection") -> {
                discover_movie_progress_bar.visibility = View.GONE
                Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show()
            }
            NetworkState.serverError("Server Error") -> {
                discover_movie_progress_bar.visibility = View.GONE
            }
        }
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }

    private fun viewErrorLanguageAdjustment() {
        if (currentLanguage == languageEnglishValue){
            movie_error_button.text = getString(R.string.button_try_again_en)
            movie_error_text.text = getString(R.string.no_internet_connection_en)
        }
        else {
            movie_error_button.text = getString(R.string.button_try_again_id)
            movie_error_text.text = getString(R.string.no_internet_connection_id)
        }

    }
}