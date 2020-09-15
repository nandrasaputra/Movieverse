package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.utils.Constant
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.discover.DiscoverFragmentDirections
import com.nandra.movieverse.util.FilmType
import kotlinx.android.synthetic.main.item_discover_recyclerview.view.*

class DiscoverViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bindView(film: FilmGist?, filmType: FilmType) {
        film?.let {
            when(filmType) {
                is FilmType.FilmTypeMovie -> { bindMovieViewProperties( it) }
                is FilmType.FilmTypeTV -> { bindTVShowViewProperties(it) }
            }
        }
    }

    private fun bindMovieViewProperties(film: FilmGist) {
        itemView.apply {
            item_grid_title.text = film.movieTitle
            val url = "https://image.tmdb.org/t/p/w185"
            if(film.posterImagePath.isNotEmpty()) {
                Glide.with(view)
                    .load(url + film.posterImagePath)
                    .apply(RequestOptions().override(200, 300))
                    .into(item_grid_poster)
            } else {
                Glide.with(view)
                    .load(R.drawable.img_back_portrait_default)
                    .apply(RequestOptions().override(200, 300))
                    .into(item_grid_poster)
            }
            setOnClickListener {
                val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragmentInDiscover(Constant.MOVIE_FILM_TYPE, film.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun bindTVShowViewProperties(film: FilmGist) {
        itemView.apply {
            item_grid_title.text = film.tvTitle
            val url = "https://image.tmdb.org/t/p/w185"
            if(film.posterImagePath.isNotEmpty()) {
                Glide.with(view)
                    .load(url + film.posterImagePath)
                    .apply(RequestOptions().override(200, 300))
                    .into(item_grid_poster)
            } else {
                Glide.with(view)
                    .load(R.drawable.img_back_portrait_default)
                    .apply(RequestOptions().override(200, 300))
                    .into(item_grid_poster)
            }
            view.setOnClickListener {
                val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragmentInDiscover(Constant.TV_FILM_TYPE, film.id)
                findNavController().navigate(action)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup) : DiscoverViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_discover_recyclerview, parent, false)
            return DiscoverViewHolder(view)
        }
    }
}
