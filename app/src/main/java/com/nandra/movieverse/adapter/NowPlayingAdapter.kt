package com.nandra.movieverse.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.github.islamkhsh.CardSliderAdapter
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Film
import kotlinx.android.synthetic.main.item_now_playing.view.*

class NowPlayingAdapter(data: ArrayList<Film>) : CardSliderAdapter<Film>(data) {
    override fun bindView(position: Int, itemContentView: View, item: Film?) {
        item?.run {
            if(item.posterPath != null) {
                val baseImageUrl = "https://image.tmdb.org/t/p/w500"
                Glide.with(itemContentView.context)
                    .load(baseImageUrl + item.posterPath)
                    .into(itemContentView.item_now_playing_poster)
            }
            itemContentView.item_now_playing_title.text = item.title
        }
    }

    override fun getItemContentLayout(position: Int) = R.layout.item_now_playing
}