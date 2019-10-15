package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.ui.DiscoverFragmentDirections
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.item_discover_recyclerview.view.*

class DiscoverPagedListAdapter : PagedListAdapter<Film, DiscoverPagedListAdapter.MyViewHolder>(DiffUtilCallback()) {

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
        holder.itemView.item_grid_title.text = currentFilm.title
        val url = "https://image.tmdb.org/t/p/w185"
        if(currentFilm.posterPath != null) {
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterPath)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(holder.itemView.item_grid_poster)
        }
        holder.itemView.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(Constant.MOVIE_FILM_TYPE).setId(currentFilm.id.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class DiffUtilCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.title == newItem.title
        }

    }
}