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
import com.nandra.movieverse.adapter.FavoriteMovieRecyclerViewAdapter
import com.nandra.movieverse.database.FavoriteMovie
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_favorite_movie.*

class FavoriteMovieFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener, FavoriteMovieRecyclerViewAdapter.IFavoriteMovieRecyclerViewAdapterCallback {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var listMovie: List<FavoriteMovie> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.movieFavoriteList.observe(this) {
            listMovie = it
            handleFavoriteMovieListChanged(it)
        }
    }

    private fun attemptPrepareView() {
        checkFavoriteListItem(sharedViewModel.movieFavoriteList.value)
        favorite_movie_recyclerview.swapAdapter(FavoriteMovieRecyclerViewAdapter(listMovie, this), true)
    }

    private fun checkFavoriteListItem(data: List<FavoriteMovie>?) {
        when {
            data == null -> favorite_movie_no_item_back.visibility = View.GONE
            data.isEmpty() -> {
                favorite_movie_no_item_back.visibility = View.VISIBLE
                favorite_movie_no_item_text.text = getString(R.string.favorite_no_item_en)
            }
            else -> favorite_movie_no_item_back.visibility = View.GONE
        }
    }

    private fun handleFavoriteMovieListChanged(data: List<FavoriteMovie>) {
        checkFavoriteListItem(data)
        favorite_movie_recyclerview.swapAdapter(FavoriteMovieRecyclerViewAdapter(data, this), true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        favorite_movie_recyclerview.layoutManager = LinearLayoutManager(context)
        attemptPrepareView()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        attemptPrepareView()
    }

    private fun prepareSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onAdapterDeleteButtonPressed(position: Int) {
        sharedViewModel.deleteFavoriteMovie(listMovie[position])
    }
}