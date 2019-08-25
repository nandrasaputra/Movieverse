package com.nandra.moviecatalogue

import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nandra.moviecatalogue.adapter.RecyclerViewAdapter
import com.nandra.moviecatalogue.model.Film

class MovieFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var dataMoviesTitle: Array<String>
    private lateinit var dataMoviesRating: Array<String>
    private lateinit var dataMoviesGenre: Array<String>
    private lateinit var dataMoviesOverview: Array<String>
    private lateinit var dataMoviesPoster: TypedArray
    private var moviesList: ArrayList<Film> = arrayListOf()
    private lateinit var movieRecyclerView : RecyclerView
    private lateinit var sharedPreferences : SharedPreferences
    private var currentLanguage : String? = ""
    private lateinit var languageKey : String
    private lateinit var languageBahasaValue : String
    private lateinit var languageEnglishValue : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        movieRecyclerView = view.findViewById(R.id.movie_recyclerview)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        prepareMovieListView(currentLanguage!!)
        movieRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerViewAdapter(moviesList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(activity)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)
        if (currentLanguage!! == languageEnglishValue) {
            prepareMovieListView(currentLanguage!!)
            movieRecyclerView.swapAdapter(RecyclerViewAdapter(moviesList), true)
        } else {
            prepareMovieListView(currentLanguage!!)
            movieRecyclerView.swapAdapter(RecyclerViewAdapter(moviesList), true)
        }
    }

    private fun prepareMovieListView(languagePref: String) {
        if(languagePref == getString(R.string.preferences_language_value_english)) {
            dataMoviesGenre = resources.getStringArray(R.array.all_movie_genre_array_en)
            dataMoviesOverview = resources.getStringArray(R.array.all_movie_overview_array_en)
        } else {
            dataMoviesGenre = resources.getStringArray(R.array.all_movie_genre_array_id)
            dataMoviesOverview = resources.getStringArray(R.array.all_movie_overview_array_id)
        }
        dataMoviesTitle = resources.getStringArray(R.array.all_movie_title_array)
        dataMoviesRating = resources.getStringArray(R.array.all_movie_rating_array)
        dataMoviesPoster = resources.obtainTypedArray(R.array.all_movie_poster_array)
        val mMovieShowList: ArrayList<Film> = arrayListOf()
        for (i in dataMoviesTitle.indices) {
            val mTitle = dataMoviesTitle[i]
            val mRating = dataMoviesRating[i]
            val mGenre = dataMoviesGenre[i]
            val mOverview = dataMoviesOverview[i]
            val mPoster = dataMoviesPoster.getResourceId(i, -1)
            mMovieShowList.add(Film(mTitle, mRating, mGenre, mOverview, mPoster))
        }
        moviesList = mMovieShowList
    }

    private fun prepareSharedPreferences() {
        languageKey = getString(R.string.preferences_language_key)
        languageBahasaValue = getString(R.string.preferences_language_value_bahasaindonesia)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        currentLanguage = sharedPreferences.getString(getString(R.string.preferences_language_key),
            getString(R.string.preferences_language_value_english))
    }

}