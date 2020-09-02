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

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var discoverMoviePagedListAdapter: DiscoverPagedListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover_movie, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.discoverMoviePagingListLiveData.observe(this) { discoverMoviePagedListAdapter.submitList(it) }
        sharedViewModel.discoverMovieNetworkStateLiveData.observe(this) {
            discoverMoviePagedListAdapter.setNetworkState(it)
            handleNetworkState(it)
        }
        sharedViewModel.discoverMovieIsInitialDataLoadedLiveData.observe(this) {  }
        discoverMoviePagedListAdapter = DiscoverPagedListAdapter(Constant.MOVIE_FILM_TYPE) {sharedViewModel.retryLoadAllFailed()}
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        discover_movie_recyclerview.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = discoverMoviePagedListAdapter
        }
        checkErrorState()
        viewErrorLanguageAdjustment()
        sharedViewModel.discoverMovieNetworkStateLiveData.value?.run {
            handleNetworkState(this)
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    private fun handleNetworkState(state: NetworkState) {

        val isInitialDataLoaded = sharedViewModel.discoverMovieIsInitialDataLoadedLiveData.value ?: false

        when(state) {
            NetworkState.LOADING -> {
                if(!isInitialDataLoaded){
                    discover_movie_error_back.visibility = View.GONE
                    discover_movie_progress_bar.visibility = View.VISIBLE
                    discover_movie_cover.visibility = View.VISIBLE
                }
            }
            NetworkState.LOADED -> {
                if (isInitialDataLoaded){
                    discover_movie_progress_bar.visibility = View.GONE
                    discover_movie_cover.visibility = View.GONE
                }
            }
            NetworkState.FAILED -> {
                discover_movie_progress_bar.visibility = View.GONE
                if(!isInitialDataLoaded) {
                    discover_movie_error_back.visibility = View.VISIBLE
                    viewErrorLanguageAdjustment()
                    discover_movie_error_button.setOnClickListener {
                        sharedViewModel.retryLoadAllFailed()
                    }
                } else {
                    Toast.makeText(activity, "Cannot Get Data From Server", Toast.LENGTH_SHORT).show()
                }
            }
            NetworkState.CANNOT_CONNECT -> {
                discover_movie_progress_bar.visibility = View.GONE
                if(!isInitialDataLoaded) {
                    discover_movie_error_back.visibility = View.VISIBLE
                    viewErrorLanguageAdjustment()
                    discover_movie_error_button.setOnClickListener {
                        sharedViewModel.retryLoadAllFailed()
                    }
                } else {
                    Toast.makeText(activity, "Cannot Connect To Server", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun prepareSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private fun checkErrorState() {
        if (sharedViewModel.discoverMovieIsInitialDataLoadedLiveData.value != null && sharedViewModel.discoverMovieNetworkStateLiveData.value != null) {
            if (!sharedViewModel.discoverMovieIsInitialDataLoadedLiveData.value!! && (sharedViewModel.discoverMovieNetworkStateLiveData.value!! == NetworkState.FAILED
                        || sharedViewModel.discoverMovieNetworkStateLiveData.value!! == NetworkState.CANNOT_CONNECT))
            {
                discover_movie_error_back.visibility = View.VISIBLE
                discover_movie_error_button.setOnClickListener {
                    sharedViewModel.retryLoadAllFailed()
                }
            }
        }
    }

    private fun viewErrorLanguageAdjustment() {
        discover_movie_error_button.text = getString(R.string.button_try_again_en)
        discover_movie_error_text.text = getString(R.string.no_internet_connection_en)
    }

}