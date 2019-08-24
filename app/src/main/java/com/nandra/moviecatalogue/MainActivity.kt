package com.nandra.moviecatalogue

import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dataMoviesTitle: Array<String>
    private lateinit var dataMoviesRating: Array<String>
    private lateinit var dataMoviesGenre: Array<String>
    private lateinit var dataMoviesOverview: Array<String>
    private lateinit var dataMoviesPoster: TypedArray
    private lateinit var moviesAdapter: MainMovieListAdapter
    private var moviesList: ArrayList<Film> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareMovieListView()
        main_listview.adapter = moviesAdapter
    }

    private fun prepareMovieListView() {
        dataMoviesTitle = resources.getStringArray(R.array.all_movie_title_array)
        dataMoviesRating = resources.getStringArray(R.array.all_movie_rating_array)
        dataMoviesGenre = resources.getStringArray(R.array.all_movie_genre_array)
        dataMoviesOverview = resources.getStringArray(R.array.all_movie_overview_array)
        dataMoviesPoster = resources.obtainTypedArray(R.array.all_movie_poster_array)

        for (i in dataMoviesTitle.indices) {

            val mTitle = dataMoviesTitle[i]
            val mRating = dataMoviesRating[i]
            val mGenre = dataMoviesGenre[i]
            val mOverview = dataMoviesOverview[i]
            val mPoster = dataMoviesPoster.getResourceId(i, -1)

            val movie = Film(mTitle, mRating, mGenre, mOverview, mPoster)
            moviesList.add(movie)
        }

        moviesAdapter = MainMovieListAdapter(this, moviesList)
    }

    companion object {
        @JvmStatic val EXTRA_MOVIE = "extra_movie"
    }
}
