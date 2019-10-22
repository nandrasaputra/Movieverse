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
import com.nandra.movieverse.adapter.DiscoverAdapter2
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
    private lateinit var discoverMovieAdapter: DiscoverAdapter2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover_movie, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.movieDiscoverPagingList.observe(this, Observer {
            discoverMovieAdapter.submitList(it)
        })
        sharedViewModel.movieNetworkState.observe(this, Observer {
            discoverMovieAdapter.setNetworkState(it)
            handleNetworkState(it)
        })
        sharedViewModel.movieIsInitailLoaded.observe(this, Observer {
            /*if(!it) {
                discover_movie_cover.visibility = View.VISIBLE
            } else {
                discover_movie_cover.visibility = View.GONE
            }*/
        })
        discoverMovieAdapter = DiscoverAdapter2(Constant.MOVIE_FILM_TYPE) {sharedViewModel.retryLoadAllFailed()}
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        movie_recyclerview.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
        movie_recyclerview.adapter = discoverMovieAdapter
        checkErrorState()
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
            NetworkState.LOADING -> {
                if(!sharedViewModel.movieIsInitailLoaded.value!!){
                    movie_error_back.visibility = View.GONE
                    discover_movie_progress_bar.visibility = View.VISIBLE
                    discover_movie_cover.visibility = View.VISIBLE
                }
            }
            NetworkState.LOADED -> {
                if (sharedViewModel.movieIsInitailLoaded.value!!){
                    discover_movie_progress_bar.visibility = View.GONE
                    discover_movie_cover.visibility = View.GONE
                }
            }
            NetworkState.FAILED -> {
                discover_movie_progress_bar.visibility = View.GONE
                if(!sharedViewModel.movieIsInitailLoaded.value!!) {
                    movie_error_back.visibility = View.VISIBLE
                    viewErrorLanguageAdjustment()
                    movie_error_button.setOnClickListener {
                        sharedViewModel.retryLoadAllFailed()
                    }
                }
                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show()
            }
            NetworkState.SERVER_ERROR -> {
                discover_movie_progress_bar.visibility = View.GONE
                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show()
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

    private fun checkErrorState() {
        if (sharedViewModel.movieIsInitailLoaded.value != null && sharedViewModel.movieNetworkState.value != null) {
            if (!sharedViewModel.movieIsInitailLoaded.value!! && (sharedViewModel.movieNetworkState.value!! == NetworkState.FAILED
                        || sharedViewModel.movieNetworkState.value!! == NetworkState.SERVER_ERROR))
            {
                movie_error_back.visibility = View.VISIBLE
                movie_error_button.setOnClickListener {
                    sharedViewModel.retryLoadAllFailed()
                }
            }
        }
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