package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.ui.DiscoverFragmentDirections
import com.nandra.movieverse.util.Constant

class DiscoverViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val itemGridTitle: TextView = view.findViewById(R.id.item_grid_title)
    private val itemGridPoster: RoundedImageView = view.findViewById(R.id.item_grid_poster)

    fun bindView(film: Film?, filmType: String) {
        val typeMovie = view.context.getString(R.string.film_type_movie)
        film?.let {
            if(filmType == typeMovie)
                bindMovieViewProperties(view, film)
            else
                bindTVShowViewProperties(view, film)
        }
    }

    private fun bindMovieViewProperties(view: View, film: Film) {
        itemGridTitle.text = film.title
        val url = "https://image.tmdb.org/t/p/w185"
        if(!film.posterPath.isNullOrEmpty()) {
            Glide.with(view)
                .load(url + film.posterPath)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(itemGridPoster)
        } else {
            Glide.with(view)
                .load(R.drawable.img_back_portrait_default)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(itemGridPoster)
        }
        view.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(Constant.MOVIE_FILM_TYPE).setId(film.id.toString())
            view.findNavController().navigate(action)
        }
    }

    private fun bindTVShowViewProperties(view: View, film: Film) {
        itemGridTitle.text = film.tvName
        val url = "https://image.tmdb.org/t/p/w185"
        if(!film.posterPath.isNullOrEmpty()) {
            Glide.with(view)
                .load(url + film.posterPath)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(itemGridPoster)
        } else {
            Glide.with(view)
                .load(R.drawable.img_back_portrait_default)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(itemGridPoster)
        }
        view.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(Constant.TV_FILM_TYPE).setId(film.id.toString())
            view.findNavController().navigate(action)
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