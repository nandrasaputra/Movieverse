package com.nandra.moviecatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.network.Film
import com.nandra.moviecatalogue.ui.DiscoverFragmentDirections
import kotlinx.android.synthetic.main.item_main_movie_list.view.*

class RecyclerViewAdapter(
    private val filmList : ArrayList<Film>,
    private val filmType: String,
    private val genreList: ArrayList<String>
) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_movie_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFilm = filmList[position]
        val currentGenre = genreList[position]
        val typeMovie = holder.itemView.context.getString(R.string.film_type_movie)

        if(filmType == typeMovie)
            bindMovieViewProperties(holder, position, currentFilm, currentGenre)
        else
            bindTVShowViewProperties(holder, position, currentFilm, currentGenre)
    }

    private fun bindTVShowViewProperties(holder: MyViewHolder, position: Int, currentFilm: Film, currentGenre: String) {
        holder.itemView.item_text_movie_title.text = currentFilm.tvName
        holder.itemView.item_text_movie_rating.text = currentFilm.voteAverage.toString()
        holder.itemView.item_text_movie_genre.text = currentGenre
        val url = "https://image.tmdb.org/t/p/w185"
        Glide.with(holder.itemView)
            .load(url + currentFilm.posterPath)
            .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
            .into(holder.itemView.item_image_movie_poster)
        if(currentFilm.overview == ""){
            val text = holder.itemView.context.getString(R.string.overview_not_available_id)
            holder.itemView.item_text_movie_overview.text = text
        } else {
            holder.itemView.item_text_movie_overview.text = currentFilm.overview
        }
        holder.itemView.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(filmType).setPosition(position)
            holder.itemView.findNavController().navigate(action)
        }
    }

    private fun bindMovieViewProperties(holder: MyViewHolder, position: Int, currentFilm: Film, currentGenre: String) {
        holder.itemView.item_text_movie_title.text = currentFilm.title
        holder.itemView.item_text_movie_rating.text = currentFilm.voteAverage.toString()
        holder.itemView.item_text_movie_genre.text = currentGenre
        val url = "https://image.tmdb.org/t/p/w185"
        Glide.with(holder.itemView)
            .load(url + currentFilm.posterPath)
            .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
            .into(holder.itemView.item_image_movie_poster)
        if(currentFilm.overview == ""){
            val text = holder.itemView.context.getString(R.string.overview_not_available_id)
            holder.itemView.item_text_movie_overview.text = text
        } else {
            holder.itemView.item_text_movie_overview.text = currentFilm.overview
        }
        holder.itemView.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(filmType).setPosition(position)
            holder.itemView.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}