package com.nandra.moviecatalogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.res.TypedArray
import android.widget.ListView


class MainActivity : AppCompatActivity() {

    private lateinit var dataMoviesTitle: Array<String>
    private lateinit var dataMoviesRating: Array<String>
    private lateinit var dataMoviesGenre: Array<String>
    private lateinit var dataMoviesOverview: Array<String>
    private lateinit var dataMoviesPoster: TypedArray
    private lateinit var moviesAdapter: MainMovieListAdapter
    private var moviesList: ArrayList<Movie> = arrayListOf()
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareMovieListView()

        listView = findViewById(R.id.main_listview)
        listView.adapter = moviesAdapter
    }

    fun prepareMovieListView() {
        dataMoviesTitle = resources.getStringArray(R.array.string_movie_title)
        dataMoviesRating = resources.getStringArray(R.array.string_movie_rating)
        dataMoviesGenre = resources.getStringArray(R.array.string_movie_genre)
        dataMoviesOverview = resources.getStringArray(R.array.string_movie_overview)
        dataMoviesPoster = resources.obtainTypedArray(R.array.string_movie_poster)

        for (i in 0 until dataMoviesTitle.size) {

            val mTitle = dataMoviesTitle[i]
            val mRating = dataMoviesRating[i]
            val mGenre = dataMoviesGenre[i]
            val mOverview = dataMoviesOverview[i]
            val mPoster = dataMoviesPoster.getResourceId(i, -1)

            val movie = Movie(mTitle, mRating, mGenre, mOverview, mPoster)

            moviesList.add(movie)
        }

        moviesAdapter = MainMovieListAdapter(this, moviesList)
    }
}
