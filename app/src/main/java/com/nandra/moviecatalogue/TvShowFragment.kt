package com.nandra.moviecatalogue

import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TvShowFragment : Fragment() {

    private lateinit var dataTVShowTitle: Array<String>
    private lateinit var dataTVShowRating: Array<String>
    private lateinit var dataTVShowGenre: Array<String>
    private lateinit var dataTVShowOverview: Array<String>
    private lateinit var dataTVShowPoster: TypedArray
    private var tvShowList: ArrayList<Film> = arrayListOf()
    private lateinit var tvShowRecyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tv_show, container, false)
        tvShowRecyclerView = view.findViewById(R.id.tvshow_recyclerview)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareTVShowListView()
        tvShowRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = ERecyclerViewAdapter(tvShowList)
        }
    }

    private fun prepareTVShowListView() {
        dataTVShowTitle = resources.getStringArray(R.array.all_tvshow_title_array)
        dataTVShowRating = resources.getStringArray(R.array.all_tvshow_rating_array)
        dataTVShowGenre = resources.getStringArray(R.array.all_tvshow_genre_array)
        dataTVShowOverview = resources.getStringArray(R.array.all_tvshow_overview_array)
        dataTVShowPoster = resources.obtainTypedArray(R.array.all_tvshow_poster_array)

        for (i in dataTVShowTitle.indices) {

            val mTitle = dataTVShowTitle[i]
            val mRating = dataTVShowRating[i]
            val mGenre = dataTVShowGenre[i]
            val mOverview = dataTVShowOverview[i]
            val mPoster = dataTVShowPoster.getResourceId(i, -1)

            val movie = Film(mTitle, mRating, mGenre, mOverview, mPoster)
            tvShowList.add(movie)
        }
    }

}