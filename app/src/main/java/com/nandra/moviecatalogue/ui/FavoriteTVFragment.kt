package com.nandra.moviecatalogue.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.adapter.FavoriteTVRecyclerViewAdapter
import com.nandra.moviecatalogue.database.FavoriteTV
import com.nandra.moviecatalogue.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_favorite_tv.*

class FavoriteTVFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var tvList: List<FavoriteTV> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_tv, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.tvFavoriteList.observe(this, Observer {
            tvList = it
            handleFavoriteTVListChanged(it)
        })
    }

    private fun attemptPrepareView() {
        favorite_tv_recyclerview.swapAdapter(FavoriteTVRecyclerViewAdapter(tvList, currentLanguage), true)
    }

    private fun handleFavoriteTVListChanged(tv: List<FavoriteTV>) {
        favorite_tv_recyclerview.swapAdapter(FavoriteTVRecyclerViewAdapter(tv, currentLanguage), true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        favorite_tv_recyclerview.layoutManager = LinearLayoutManager(context)
        attemptPrepareView()
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)!!
        attemptPrepareView()
    }
}