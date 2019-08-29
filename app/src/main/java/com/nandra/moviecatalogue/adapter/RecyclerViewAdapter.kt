package com.nandra.moviecatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.data.Film
import com.nandra.moviecatalogue.ui.MainFragmentDirections
import kotlinx.android.synthetic.main.item_main_movie_list.view.*

class RecyclerViewAdapter(
    private val filmList : ArrayList<Film>,
    private val filmType: String
) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_movie_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovie = filmList[position]
        holder.itemView.item_text_movie_title.text = currentMovie.title
        holder.itemView.item_text_movie_rating.text = currentMovie.voteAverage.toString()
        //holder.itemView.item_text_movie_genre.text = currentMovie.genre
        val url = "https://image.tmdb.org/t/p/w185"
        Glide.with(holder.itemView)
            .load(url + currentMovie.posterPath)
            .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
            .into(holder.itemView.item_image_movie_poster)
        holder.itemView.item_text_movie_overview.text = currentMovie.overview
        holder.itemView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(filmType).setPosition(position)
            holder.itemView.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}