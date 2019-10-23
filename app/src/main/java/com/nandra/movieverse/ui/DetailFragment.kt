package com.nandra.movieverse.ui

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.CastRecyclerViewAdapter
import com.nandra.movieverse.adapter.ImagesRecyclerViewAdapter
import com.nandra.movieverse.adapter.VideosRecyclerAdapter
import com.nandra.movieverse.network.response.DetailResponse
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.util.getStringGenre
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
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
        detail_cast_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        detail_images_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        detail_videos_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        detail_cover.setOnClickListener {}      //Prevent Click Through
        sharedViewModel.detailFilmTranslated.observe(this, Observer {
            prepareView(sharedViewModel.detailFilm.value!!)
        })
        sharedViewModel.roomState.observe(this, Observer {
            onRoomStateChange(it)
        })
        if (!sharedViewModel.isOnDetailFragment.value!!) {
            attemptPrepareView()
        } else if(sharedViewModel.isOnDetailFragment.value!! && sharedViewModel.detailState.value == Constant.STATE_NO_CONNECTION) {
            attemptPrepareView()
        }
        sharedViewModel.detailState.observe(this, Observer {
            handleState(it)
        })
        if (filmType == Constant.MOVIE_FILM_TYPE) {
            sharedViewModel.movieFavoriteList.observe(this, Observer {
                checkFavoriteState()
            })
        } else {
            sharedViewModel.tvFavoriteList.observe(this, Observer {
                checkFavoriteState()
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (!sharedViewModel.isOnDetailFragment.value!! || sharedViewModel.detailState.value == Constant.STATE_NO_CONNECTION
            || sharedViewModel.detailState.value == Constant.STATE_SERVER_ERROR){
            detail_cover.visibility = View.VISIBLE
            sharedViewModel.isOnDetailFragment.value = true
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
                if(currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE){
                    Toast.makeText(activity, "Added Into Favorite List", Toast.LENGTH_SHORT).show()
                    sharedViewModel.roomState.value = SharedViewModel.RoomState.StandBy
                }
                else {
                    Toast.makeText(activity, "Berhasil Ditambahkan Ke List Favorit", Toast.LENGTH_SHORT).show()
                    sharedViewModel.roomState.value = SharedViewModel.RoomState.StandBy
                }
            }
            SharedViewModel.RoomState.Failure -> {
                if(currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE){
                    Toast.makeText(activity, "Failed To Add Into Favorite List", Toast.LENGTH_SHORT).show()
                    sharedViewModel.roomState.value = SharedViewModel.RoomState.StandBy
                }
                else {
                    Toast.makeText(activity, "Gagal Menambahkan Ke List Favorit", Toast.LENGTH_SHORT).show()
                    sharedViewModel.roomState.value = SharedViewModel.RoomState.StandBy
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)!!
        prepareView(sharedViewModel.detailFilm.value!!)
    }

    private fun checkFavoriteState() {
        val data = sharedViewModel.detailFilm.value
        val state = if (filmType == Constant.MOVIE_FILM_TYPE) {
            sharedViewModel.movieFavoriteList.value?.any {
                    x -> x.id == data?.id.toString()
            }
        } else {
            sharedViewModel.tvFavoriteList.value?.any { x -> x.id == data?.id.toString() }
        }
        if (state != null && state == true) {
            detail_image_heart.setImageResource(R.drawable.ic_heart_pink)
            if (currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE)
                detail_favorite_text.text = getString(R.string.favorite_text_remove_to_favorite_en)
            else
                detail_favorite_text.text = getString(R.string.favorite_text_remove_to_favorite_id)
            detail_favorite_section.setOnClickListener {
                if (filmType == Constant.MOVIE_FILM_TYPE){
                    val movie = sharedViewModel.movieFavoriteList.value!!.first { it.id == data?.id.toString() }
                    sharedViewModel.deleteFavoriteMovie(movie)
                } else {
                    val tv = sharedViewModel.tvFavoriteList.value!!.first { it.id == data?.id.toString() }
                    sharedViewModel.deleteFavoriteTV(tv)
                }
                if (currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE)
                    Toast.makeText(activity, getString(R.string.removed_from_favorite_en), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(activity, getString(R.string.removed_from_favorite_id), Toast.LENGTH_SHORT).show()
            }
        } else {
            detail_image_heart.setImageResource(R.drawable.ic_heart_hollow)
            if (currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE)
                detail_favorite_text.text = getString(R.string.favorite_text_add_to_favorite_en)
            else
                detail_favorite_text.text = getString(R.string.favorite_text_add_to_favorite_id)
            detail_favorite_section.setOnClickListener {
                saveToFavorite()
            }
        }
    }

    private fun prepareView(data: DetailResponse) {
        if(filmType == Constant.MOVIE_FILM_TYPE) {
            detail_text_movie_title.text = data.title
            detail_text_release_date.text = formatDate(data.releaseDate)
            val runtime = if( currentLanguage == languageEnglishValue)
                "${data.runtime} Minutes"
            else
                "${data.runtime} Menit"
            detail_text_runtime.text = runtime
        } else {
            detail_text_movie_title.text = data.tvTitle
            detail_text_release_date.text = formatDate(data.tvAirDate)
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
            detail_fragment_cast_text.text = getString(R.string.detail_cast_en)
            detail_overview_text.text = getString(R.string.detail_overview_en)
            detail_gallery_text.text = getString(R.string.detail_gallery_en)
            detail_video_text.text = getString(R.string.detail_videos_en)
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
            detail_fragment_cast_text.text = getString(R.string.detail_cast_id)
            detail_overview_text.text = getString(R.string.detail_overview_id)
            detail_gallery_text.text = getString(R.string.detail_gallery_id)
            detail_video_text.text = getString(R.string.detail_videos_id)
        }
        val url = "https://image.tmdb.org/t/p/w342"
        val backdropUrl = "https://image.tmdb.org/t/p/w500"
        if(data.posterPath != null) {
            Glide.with(this)
                .load(url + data.posterPath)
                .into(detail_image_movie_poster)
        } else {
            Glide.with(this)
                .load(R.drawable.img_back_portrait_default)
                .into(detail_image_movie_poster)
        }
        if(data.backdropPath != null) {
            Glide.with(this)
                .load(backdropUrl + data.backdropPath)
                .into(detail_backdrop)
        } else {
            Glide.with(this)
                .load(R.drawable.img_back_landscape_default)
                .into(detail_backdrop)
        }
        val castList = when {
            data.credits.cast.size > 20 -> {
                detail_fragment_cast_text.visibility = View.VISIBLE
                data.credits.cast.take(20)
            }
            data.credits.cast.isNotEmpty() -> {
                detail_fragment_cast_text.visibility = View.VISIBLE
                data.credits.cast
            }
            else -> {
                detail_fragment_cast_text.visibility = View.GONE
                data.credits.cast
            }
        }
        val imageList = when {
            data.images.backdrops.size > 10 -> {
                detail_gallery_text.visibility = View.VISIBLE
                data.images.backdrops.take(10)
            }
            data.images.backdrops.isNotEmpty() -> {
                detail_gallery_text.visibility = View.VISIBLE
                data.images.backdrops
            }
            else -> {
                detail_gallery_text.visibility = View.GONE
                data.images.backdrops
            }
        }
        val videoList = when {
            data.videos.videoData.size > 3 -> {
                detail_video_text.visibility = View.VISIBLE
                data.videos.videoData.take(3)
            }
            data.videos.videoData.isNotEmpty() -> {
                detail_video_text.visibility = View.VISIBLE
                data.videos.videoData
            }
            else -> {
                detail_video_text.visibility = View.GONE
                data.videos.videoData
            }
        }
        detail_images_recyclerview.swapAdapter(ImagesRecyclerViewAdapter(imageList), true)
        detail_cast_recyclerview.swapAdapter(CastRecyclerViewAdapter(castList), true)
        detail_videos_recyclerview.swapAdapter(VideosRecyclerAdapter(videoList), false)
        checkFavoriteState()
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDate(stringDate: String) : String {
        val inFormat = SimpleDateFormat("yyyy-MM-dd")
        val outFormat = SimpleDateFormat("dd-MM-yyyy")

        return try {
            val date = inFormat.parse(stringDate)
            outFormat.format(date ?: "")
        } catch (exp: Exception) {
            ""
        }
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