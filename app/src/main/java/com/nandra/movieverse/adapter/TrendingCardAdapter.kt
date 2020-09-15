package com.nandra.movieverse.adapter

import android.view.View
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.utils.Constant
import com.github.islamkhsh.CardSliderAdapter
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.home.HomeFragmentDirections
import kotlinx.android.synthetic.main.item_home_trending.view.*

class TrendingCardAdapter(private var data: ArrayList<FilmGist>) : CardSliderAdapter<FilmGist>(data) {
    override fun bindView(position: Int, itemContentView: View, item: FilmGist?) {
        item?.run {
            if(item.backdropImagePath.isNotEmpty()) {
                val baseImageUrl = "https://image.tmdb.org/t/p/w780"
                Glide.with(itemContentView.context)
                    .load(baseImageUrl + item.backdropImagePath)
                    .into(itemContentView.item_home_trending_poster)
            }
            if(item.mediaType == Constant.MOVIE_FILM_TYPE){
                itemContentView.item_home_trending_title.text = item.movieTitle
            } else {
                itemContentView.item_home_trending_title.text = item.tvTitle
            }

            itemContentView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragmentInHome(mediaType, id)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemContentLayout(position: Int) = R.layout.item_home_trending

    fun submitList(newData: ArrayList<FilmGist>) {
        data = newData
        super.notifyDataSetChanged()
    }
}
