package com.nandra.moviecatalogue.ui

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
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.model.FilmUsedTo

class TvShowFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var dataTVShowTitle: Array<String>
    private lateinit var dataTVShowRating: Array<String>
    private lateinit var dataTVShowGenre: Array<String>
    private lateinit var dataTVShowOverview: Array<String>
    private lateinit var dataTVShowPoster: TypedArray
    private var tvShowList: ArrayList<FilmUsedTo> = arrayListOf()
    private lateinit var tvShowRecyclerView : RecyclerView
    private lateinit var sharedPreferences : SharedPreferences
    private var currentLanguage : String? = ""
    private lateinit var languageKey : String
    private lateinit var languageBahasaValue : String
    private lateinit var languageEnglishValue : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tv_show, container, false)
        tvShowRecyclerView = view.findViewById(R.id.tvshow_recyclerview)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        prepareTVShowListView(currentLanguage!!)
        tvShowRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            //adapter = RecyclerViewAdapter(tvShowList)
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
            prepareTVShowListView(currentLanguage!!)
            //tvShowRecyclerView.swapAdapter(RecyclerViewAdapter(tvShowList), true)
        } else {
            prepareTVShowListView(currentLanguage!!)
            //tvShowRecyclerView.swapAdapter(RecyclerViewAdapter(tvShowList), true)
        }
    }

    private fun prepareTVShowListView(languagePref: String) {
        if (languagePref == languageEnglishValue) {
            dataTVShowGenre = resources.getStringArray(R.array.all_tvshow_genre_array_en)
            dataTVShowOverview = resources.getStringArray(R.array.all_tvshow_overview_array_en)
        } else {
            dataTVShowGenre = resources.getStringArray(R.array.all_tvshow_genre_array_id)
            dataTVShowOverview = resources.getStringArray(R.array.all_tvshow_overview_array_id)
        }
        dataTVShowTitle = resources.getStringArray(R.array.all_tvshow_title_array)
        dataTVShowRating = resources.getStringArray(R.array.all_tvshow_rating_array)
        dataTVShowPoster = resources.obtainTypedArray(R.array.all_tvshow_poster_array)
        val mTVShowList : ArrayList<FilmUsedTo> = arrayListOf()
        for (i in dataTVShowTitle.indices) {
            val mTitle = dataTVShowTitle[i]
            val mRating = dataTVShowRating[i]
            val mGenre = dataTVShowGenre[i]
            val mOverview = dataTVShowOverview[i]
            val mPoster = dataTVShowPoster.getResourceId(i, -1)
            mTVShowList.add(FilmUsedTo(mTitle, mRating, mGenre, mOverview, mPoster))
        }
        tvShowList = mTVShowList
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