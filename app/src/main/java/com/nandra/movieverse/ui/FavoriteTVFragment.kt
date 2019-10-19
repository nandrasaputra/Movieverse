package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.FavoriteTVRecyclerViewAdapter
import com.nandra.movieverse.database.FavoriteTV
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_favorite_tv.*

class FavoriteTVFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener, FavoriteTVRecyclerViewAdapter.IFavoriteTVRecyclerViewAdapterCallback {

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
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.tvFavoriteList.observe(this, Observer {
            tvList = it
            handleFavoriteTVListChanged(it)
        })
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    private fun attemptPrepareView() {
        checkFavoriteListItem(sharedViewModel.tvFavoriteList.value)
        favorite_tv_recyclerview.swapAdapter(FavoriteTVRecyclerViewAdapter(tvList, currentLanguage, this), true)
    }

    private fun checkFavoriteListItem(data: List<FavoriteTV>?) {
        when {
            data == null -> favorite_tv_no_item_back.visibility = View.GONE
            data.isEmpty() -> {
                favorite_tv_no_item_back.visibility = View.VISIBLE
                if(currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE)
                    favorite_tv_no_item_text.text = getString(R.string.favorite_no_item_en)
                else
                    favorite_tv_no_item_text.text = getString(R.string.favorite_no_item_id)
            }
            else -> favorite_tv_no_item_back.visibility = View.GONE
        }
    }

    private fun handleFavoriteTVListChanged(tv: List<FavoriteTV>) {
        checkFavoriteListItem(tv)
        favorite_tv_recyclerview.swapAdapter(FavoriteTVRecyclerViewAdapter(tv, currentLanguage, this), true)
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

    override fun onAdapterDeleteButtonPressed(position: Int) {
        sharedViewModel.deleteFavoriteTV(tvList[position])
    }
}