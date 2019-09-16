package com.nandra.moviecatalogue.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.network.response.DetailResponse
import com.nandra.moviecatalogue.util.Constant
import com.nandra.moviecatalogue.util.getStringGenre
import com.nandra.moviecatalogue.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentLanguage: String = ""
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
        detail_favorite_section.setOnClickListener {
            saveToFavorite()
        }
        id = DetailFragmentArgs.fromBundle(arguments!!).id
        filmType = DetailFragmentArgs.fromBundle(arguments!!).filmType
        sharedViewModel.detailState.observe(this, Observer {
            handleState(it)
        })
        sharedViewModel.detailFilmTranslated.observe(this, Observer {
            prepareView(sharedViewModel.detailFilm.value!!)
        })
        sharedViewModel.roomState.observe(this, Observer {
            onRoomStateChange(it)
        })
        if (!sharedViewModel.isOnDetailFragment) {
            attemptPrepareView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onResume() {
        super.onResume()
        if (!sharedViewModel.isOnDetailFragment || sharedViewModel.detailState.value == Constant.STATE_NO_CONNECTION
            || sharedViewModel.detailState.value == Constant.STATE_SERVER_ERROR){
            detail_cover.visibility = View.VISIBLE
            sharedViewModel.isOnDetailFragment = true
        }
    }

    private fun attemptPrepareView() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
            sharedViewModel.requestDetail(id, filmType)
        }
    }

    private fun saveToFavorite() {
        val data = sharedViewModel.detailFilm.value!!
        val indonesiaGenre = sharedViewModel.detailFilmTranslated.value!!.text[1]
        val indonesiaOverview = sharedViewModel.detailFilmTranslated.value!!.text[0]
        if(filmType == Constant.MOVIE_FILM_TYPE) {
            sharedViewModel.saveToFavorite(data, Constant.MOVIE_FILM_TYPE, indonesiaGenre, indonesiaOverview)
        } else {
            sharedViewModel.saveToFavorite(data, Constant.TV_FILM_TYPE, indonesiaGenre, indonesiaOverview)
        }

    }

    private fun onRoomStateChange(state: SharedViewModel.RoomState?) {
        when (state) {
            SharedViewModel.RoomState.Success -> {
                Toast.makeText(activity, "Loaded", Toast.LENGTH_SHORT).show()
            }
            SharedViewModel.RoomState.Failure -> {
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
            }
            SharedViewModel.RoomState.StandBy -> {
                Toast.makeText(activity, "StandBy", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)!!
        prepareView(sharedViewModel.detailFilm.value!!)
    }

    private fun prepareView(data: DetailResponse) {
        if(filmType == Constant.MOVIE_FILM_TYPE) {
            detail_text_movie_title.text = data.title
            detail_text_release_date.text = data.releaseDate
            val runtime = if( currentLanguage == languageEnglishValue)
                "${data.runtime} Minutes"
            else
                "${data.runtime} Menit"
            detail_text_runtime.text = runtime
        } else {
            detail_text_movie_title.text = data.tvTitle
            detail_text_release_date.text = data.tvAirDate
            val totalEpisodes = if (currentLanguage == languageEnglishValue)
                "${data.tvNumberOfEpisode} Episodes"
            else
                "${data.tvNumberOfEpisode} Episode"
            detail_text_runtime.text = totalEpisodes
        }
        val rating = "${data.voteAverage} / 10"
        detail_text_movie_rating.text = rating
        if(currentLanguage == languageEnglishValue) {
            detail_text_movie_genre.text = data.genres.getStringGenre()
            if(data.overview == ""){
                val text = getString(R.string.overview_not_available_en)
                detail_text_movie_overview.text = text
            } else {
                detail_text_movie_overview.text = data.overview
            }
            val voteCount = "From ${data.voteCount} Votes"
            detail_text_movie_rating_count.text = voteCount
            detail_favorite_text.text = "Add To\nFavorite"
        } else {
            detail_text_movie_genre.text = sharedViewModel.detailFilmTranslated.value!!.text[1]
            if(data.overview == ""){
                val text = getString(R.string.overview_not_available_id)
                detail_text_movie_overview.text = text
            } else {
                detail_text_movie_overview.text = sharedViewModel.detailFilmTranslated.value!!.text[0]
            }
            val voteCount = "Dari ${data.voteCount} Suara"
            detail_text_movie_rating_count.text = voteCount
            detail_favorite_text.text = "Tambahkan Ke\nFavorit"
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

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }

    private fun handleState(state: Int) {
        when(state){
            Constant.STATE_NO_CONNECTION -> {
                detail_loading_indicator.visibility = View.GONE
                detail_shimmer.stopShimmer()
                detail_shimmer.visibility = View.GONE
                Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
            Constant.STATE_SERVER_ERROR -> {
                detail_loading_indicator.visibility = View.GONE
                detail_shimmer.stopShimmer()
                detail_shimmer.visibility = View.GONE
                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show()
            }
            Constant.STATE_SUCCESS -> {
                detail_loading_indicator.visibility = View.GONE
                detail_shimmer.stopShimmer()
                detail_shimmer.visibility = View.GONE
                detail_cover.visibility = View.GONE
            }
            Constant.STATE_LOADING -> {
                detail_cover.visibility = View.VISIBLE
                detail_shimmer.visibility = View.VISIBLE
                detail_shimmer.startShimmer()
                detail_loading_indicator.visibility = View.VISIBLE
            }
        }
    }
}