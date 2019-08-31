package com.nandra.moviecatalogue.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.ViewModel.SharedViewModel
import com.nandra.moviecatalogue.network.Film
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var film: Film

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            detail_content.visibility = View.GONE
            detail_error_button.visibility = View.VISIBLE
            detail_text_movie_rating.visibility = View.GONE
            detail_imagerounded_movie_rating_back.visibility = View.GONE
            Glide.with(this)
                .load(R.drawable.img_noconnection2)
                .into(detail_image_movie_poster)
            detail_error_button.setOnClickListener {
                if (isConnectedToInternet()) {
                    prepareView(position, filmType)
                    detail_content.visibility = View.VISIBLE
                    detail_error_button.visibility = View.GONE
                    detail_text_movie_rating.visibility = View.VISIBLE
                    detail_imagerounded_movie_rating_back.visibility = View.VISIBLE
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
        detail_text_movie_overview.text = film.overview
        val url = "https://image.tmdb.org/t/p/w342"
        Glide.with(this)
            .load(url + film.posterPath)
            .into(detail_image_movie_poster)
    }

    private fun prepareTVView(position: Int) {
        film = sharedViewModel.listTVLive.value!![position]
        val genre = sharedViewModel.tvGenreStringList[position]
        detail_text_movie_title.text = film.tvName
        detail_text_movie_genre.text = genre
        detail_text_movie_rating.text = film.voteAverage.toString()
        detail_text_movie_overview.text = film.overview
        val url = "https://image.tmdb.org/t/p/w342"
        Glide.with(this)
            .load(url + film.posterPath)
            .into(detail_image_movie_poster)
    }
}