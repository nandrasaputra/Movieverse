package com.nandra.moviecatalogue.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.network.DetailResponse
import com.nandra.moviecatalogue.network.Film
import com.nandra.moviecatalogue.network.GenreX
import com.nandra.moviecatalogue.util.Constant
import com.nandra.moviecatalogue.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var film: Film
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var id = ""
    private var filmType = ""

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
        id = DetailFragmentArgs.fromBundle(arguments!!).id
        filmType = DetailFragmentArgs.fromBundle(arguments!!).filmType
        sharedViewModel.detailFilm.observe(this, Observer {
            prepareView(it)
            detail_veil.visibility = View.GONE
        })
        attemptPrepareView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onResume() {
        super.onResume()
        if(!sharedViewModel.isOnDetailFragment){
            detail_veil.visibility = View.VISIBLE
        }
        sharedViewModel.isOnDetailFragment = true
    }

    private fun attemptPrepareView() {
        if(isConnectedToInternet()){
            val job = Job()
            val scope = CoroutineScope(Dispatchers.Main + job)
            scope.launch {
                sharedViewModel.fetchDetail(id, filmType)
            }
        } else {
            errorIndicator(true)
            detail_error_button.setOnClickListener {
                if (isConnectedToInternet()) {
                    //TODO prepareView(id, filmType)
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

    private fun prepareView(data: DetailResponse) {
        if(filmType == Constant.MOVIE_FILM_TYPE) {
            detail_text_movie_title.text = data.title
            detail_text_release_date.text = data.releaseDate
            val runtime = "${data.runtime} Minutes"
            detail_text_runtime.text = runtime
        } else {
            detail_text_movie_title.text = data.tvTitle
        }
        detail_text_movie_genre.text = data.genres.getStringGenre()
        detail_text_movie_rating.text = data.voteAverage.toString()
        if(data.overview == ""){
            val text = getString(R.string.overview_not_available_id)
            detail_text_movie_overview.text = text
        } else {
            detail_text_movie_overview.text = data.overview
        }
        val url = "https://image.tmdb.org/t/p/w342"
        val backdropUrl = "https://image.tmdb.org/t/p/w500"
        Glide.with(this)
            .load(url + data.posterPath)
            .into(detail_image_movie_poster)
        Glide.with(this)
            .load(backdropUrl + data.backdropPath)
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

    private fun List<GenreX>.getStringGenre() : String {
        val stringBuilder = StringBuilder()
        return if (this.isEmpty()) {
            "No Genre Information"
        } else {
            this.forEach {
                stringBuilder.append(it.name)
                stringBuilder.append(", ")
            }
            val result = stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length)
            result.toString()
        }
    }

}