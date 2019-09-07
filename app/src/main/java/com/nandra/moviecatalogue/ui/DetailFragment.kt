package com.nandra.moviecatalogue.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.network.Film
import com.nandra.moviecatalogue.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var film: Film
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        detail_fragment_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        detail_fragment_toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        val position = DetailFragmentArgs.fromBundle(arguments!!).position
        val filmType = DetailFragmentArgs.fromBundle(arguments!!).filmType
        attemptPrepareView(position, filmType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private fun attemptPrepareView(position: Int, filmType: String) {
        if(isConnectedToInternet()){
            prepareView(position, filmType)
        } else {
            errorIndicator(true)
            detail_error_button.setOnClickListener {
                if (isConnectedToInternet()) {
                    prepareView(position, filmType)
                    errorIndicator(false)
                }
            }
        }
    }

    private fun isConnectedToInternet() : Boolean {
        val connectivityManager = activity?.application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun prepareView(position: Int, filmType: String) {
        if (filmType == getString(R.string.film_type_movie))
            prepareMovieView(position)
        else
            prepareTVView(position)
    }

    private fun prepareMovieView(position: Int){
        film = sharedViewModel.listMovieLive.value!![position]
        val genre = sharedViewModel.movieGenreStringList[position]
        detail_text_movie_title.text = film.title
        detail_text_movie_genre.text = genre
        detail_text_movie_rating.text = film.voteAverage.toString()
        if(film.overview == ""){
            val text = getString(R.string.overview_not_available_id)
            detail_text_movie_overview.text = text
        } else {
            detail_text_movie_overview.text = film.overview
        }
        val url = "https://image.tmdb.org/t/p/w342"
        val backdropUrl = "https://image.tmdb.org/t/p/w500"
        Glide.with(this)
            .load(url + film.posterPath)
            .into(detail_image_movie_poster)
        Glide.with(this)
            .load(backdropUrl + film.backdropPath)
            .into(detail_backdrop)
    }

    private fun prepareTVView(position: Int) {
        film = sharedViewModel.listTVLive.value!![position]
        val genre = sharedViewModel.tvGenreStringList[position]
        detail_text_movie_title.text = film.tvName
        detail_text_movie_genre.text = genre
        detail_text_movie_rating.text = film.voteAverage.toString()
        if(film.overview == ""){
            val text = getString(R.string.overview_not_available_id)
            detail_text_movie_overview.text = text
        } else {
            detail_text_movie_overview.text = film.overview
        }
        val url = "https://image.tmdb.org/t/p/w342"
        val backdropUrl = "https://image.tmdb.org/t/p/w500"
        Glide.with(this)
            .load(url + film.posterPath)
            .into(detail_image_movie_poster)
        Glide.with(this)
            .load(backdropUrl + film.backdropPath)
            .into(detail_backdrop)
    }

    private fun errorIndicator(state: Boolean) {
        if (state) {
            detail_error_button.visibility = View.VISIBLE
            detail_text_movie_rating.visibility = View.GONE
            viewLanguageAdjustment()
            Glide.with(this)
                .load(R.drawable.img_noconnection2)
                .into(detail_image_movie_poster)
        } else {
            detail_error_button.visibility = View.GONE
            detail_text_movie_rating.visibility = View.VISIBLE
        }
    }

    private fun viewLanguageAdjustment() {
        if (currentLanguage == languageEnglishValue)
            detail_error_button.text = getString(R.string.button_try_again_en)
        else
            detail_error_button.text = getString(R.string.button_try_again_id)
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }
}