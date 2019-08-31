package com.nandra.moviecatalogue.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.adapter.RecyclerViewAdapter
import com.nandra.moviecatalogue.util.Constant
import com.nandra.moviecatalogue.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieFragment : Fragment() {

    private lateinit var movieRecyclerView : RecyclerView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        movieRecyclerView = view.findViewById(R.id.movie_recyclerview)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.isLoading.observe(this, Observer {
            loadingIndicator(it)
        })
        sharedViewModel.isError.observe(this, Observer {
            errorIndicator(it)
        })
        sharedViewModel.listMovieLive.observe(this, Observer {
            movieRecyclerView.swapAdapter(RecyclerViewAdapter(it, Constant.MOVIE_FILM_TYPE, sharedViewModel.movieGenreStringList), true)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        movieRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
        }
        attemptPrepareView()
    }

    private fun loadingIndicator(state: Boolean) {
        if (state) {
            movie_loading_back.visibility = View.VISIBLE
        }
        else {
            movie_loading_back.visibility = View.GONE
        }
    }

    private fun errorIndicator(state: Boolean){
        if(state){
            movie_error_back.visibility = View.VISIBLE
            viewLanguageAdjustment()
            movie_error_button.setOnClickListener {
                prepareMovieListView()
            }
        } else {
            movie_error_back.visibility = View.GONE
        }
    }

    private fun attemptPrepareView() {
        if(sharedViewModel.isError.value == true) {
            errorIndicator(sharedViewModel.isError.value!!)
            return
        }
        prepareMovieListView()
    }

    private fun prepareMovieListView() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        Glide.with(this)
            .load(R.drawable.img_loading_indicator)
            .into(movie_loading_image)
        if (sharedViewModel.isDataHasLoaded && currentLanguage == sharedViewModel.currentLanguage)
            movieRecyclerView.swapAdapter(RecyclerViewAdapter(sharedViewModel.listMovieLive.value!!, Constant.MOVIE_FILM_TYPE, sharedViewModel.movieGenreStringList), true)
        else {
            scope.launch {
                sharedViewModel.requestData(currentLanguage)
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

    private fun viewLanguageAdjustment() {
        if (currentLanguage == languageEnglishValue)
            movie_error_button.text = getString(R.string.button_try_again_en)
        else
            movie_error_button.text = getString(R.string.button_try_again_id)
    }
}