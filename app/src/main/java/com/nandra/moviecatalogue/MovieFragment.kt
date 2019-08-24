package com.nandra.moviecatalogue

import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MovieFragment : Fragment() {

    private lateinit var dataMoviesTitle: Array<String>
    private lateinit var dataMoviesRating: Array<String>
    private lateinit var dataMoviesGenre: Array<String>
    private lateinit var dataMoviesOverview: Array<String>
    private lateinit var dataMoviesPoster: TypedArray
    private var moviesList: ArrayList<Film> = arrayListOf()
    private lateinit var movieRecyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        movieRecyclerView = view.findViewById(R.id.movie_recyclerview)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareMovieListView()
        movieRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = ERecyclerViewAdapter(moviesList)
        }
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
    }

}