package com.nandra.movieverse.adapter

import android.view.View
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.islamkhsh.CardSliderAdapter
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.ui.HomeFragmentDirections
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.item_home_trending.view.*

class TrendingCardAdapter(data: ArrayList<Film>) : CardSliderAdapter<Film>(data) {
    override fun bindView(position: Int, itemContentView: View, item: Film?) {
        item?.run {
            if(item.backdropPath != null) {
                val baseImageUrl = "https://image.tmdb.org/t/p/w780"
                Glide.with(itemContentView.context)
                    .load(baseImageUrl + item.backdropPath)
                    .into(itemContentView.item_home_trending_poster)
            }
            if(item.mediaType == Constant.MOVIE_FILM_TYPE){
                itemContentView.item_home_trending_title.text = item.title
            } else {
                itemContentView.item_home_trending_title.text = item.tvName
            }
        }
        itemContentView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragmentHome(item!!.mediaType).setId(item.id.toString())
            itemContentView.findNavController().navigate(action)
        }
    }

    override fun getItemContentLayout(position: Int) = R.layout.item_home_trending
}