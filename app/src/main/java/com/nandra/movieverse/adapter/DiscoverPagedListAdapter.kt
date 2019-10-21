package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.movieverse.R
import com.nandra.movieverse.data.DiffUtilCallback
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.ui.DiscoverFragmentDirections
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.item_discover_recyclerview.view.*

class DiscoverPagedListAdapter(
    private val type: String
) : PagedListAdapter<Film, DiscoverPagedListAdapter.MyViewHolder>(DiffUtilCallback(type)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discover_recyclerview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            bindMovieViewProperties(holder, it)
        }
    }

    private fun bindMovieViewProperties(holder: MyViewHolder, currentFilm: Film) {
        if(type == Constant.MOVIE_FILM_TYPE)
            holder.itemView.item_grid_title.text = currentFilm.title
        else
            holder.itemView.item_grid_title.text = currentFilm.tvName
        val url = "https://image.tmdb.org/t/p/w185"
        if(currentFilm.posterPath != null) {
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
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(type).setId(currentFilm.id.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}