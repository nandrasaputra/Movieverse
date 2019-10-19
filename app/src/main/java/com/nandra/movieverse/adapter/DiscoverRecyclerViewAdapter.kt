package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.ui.DiscoverFragmentDirections
import kotlinx.android.synthetic.main.item_discover_recyclerview.view.*

class DiscoverRecyclerViewAdapter(
    private val filmList : ArrayList<Film>,
    private val filmType: String
) : RecyclerView.Adapter<DiscoverRecyclerViewAdapter.DiscoverViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discover_recyclerview, parent, false)
        return DiscoverViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        val currentFilm = filmList[position]
        val typeMovie = holder.itemView.context.getString(R.string.film_type_movie)

        if(filmType == typeMovie)
            bindMovieViewProperties(holder, currentFilm)
        else
            bindTVShowViewProperties(holder, currentFilm)
    }

    private fun bindMovieViewProperties(holder: DiscoverViewHolder, currentFilm: Film) {
        holder.itemView.item_grid_title.text = currentFilm.title
        val url = "https://image.tmdb.org/t/p/w185"
        if(!currentFilm.posterPath.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterPath)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(holder.itemView.item_grid_poster)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.img_back_portrait_default)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(holder.itemView.item_grid_poster)
        }
        holder.itemView.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(filmType).setId(currentFilm.id.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    private fun bindTVShowViewProperties(holder: DiscoverViewHolder, currentFilm: Film) {
        holder.itemView.item_grid_title.text = currentFilm.tvName
        val url = "https://image.tmdb.org/t/p/w185"
        if(!currentFilm.posterPath.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterPath)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(holder.itemView.item_grid_poster)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.img_back_portrait_default)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(holder.itemView.item_grid_poster)
        }
        holder.itemView.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(filmType).setId(currentFilm.id.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    class DiscoverViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
