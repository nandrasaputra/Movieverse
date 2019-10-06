package com.nandra.movieverse.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverRecyclerViewAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_discover_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DiscoverMovieFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences

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
        sharedViewModel.listMovieLive.observe(this, Observer {
            movie_recyclerview.swapAdapter(DiscoverRecyclerViewAdapter(it, Constant.MOVIE_FILM_TYPE), true)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        movie_recyclerview.apply {
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 3)
        }
        movie_searchview.setOnFocusChangeListener { view, b -> Toast.makeText(activity, "LOLZ", Toast.LENGTH_SHORT).show() }
        attemptPrepareView()
        languageAdjustment()
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
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        if (sharedViewModel.isDataHasLoaded)
            movie_recyclerview.swapAdapter(DiscoverRecyclerViewAdapter(sharedViewModel.listMovieLive.value!!, Constant.MOVIE_FILM_TYPE), true)
        else {
            scope.launch {
                sharedViewModel.requestDiscoverData()
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
        if (currentLanguage == languageEnglishValue)
            movie_error_button.text = getString(R.string.button_try_again_en)
        else
            movie_error_button.text = getString(R.string.button_try_again_id)
    }

    private fun languageAdjustment() {
        if (currentLanguage == languageEnglishValue)
            movie_searchview.queryHint = resources.getString(R.string.movie_search_view_query_hint_en)
        else
            movie_searchview.queryHint = resources.getString(R.string.movie_search_view_query_hint_id)
    }

    private fun hideKeyboard(view: View, hasFocus: Boolean, context: Context) {
        if (!hasFocus) {
            val inputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
            Toast.makeText(context, view.toString() + "," + hasFocus.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}