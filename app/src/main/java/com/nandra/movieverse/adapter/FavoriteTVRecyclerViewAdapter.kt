package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.movieverse.R
import com.nandra.movieverse.database.FavoriteTV
import com.nandra.movieverse.ui.FavoriteFragmentDirections
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.item_favorite_list.view.*

class FavoriteTVRecyclerViewAdapter(
    private var dataList : List<FavoriteTV>,
    private val callback: IFavoriteTVRecyclerViewAdapterCallback
) : RecyclerView.Adapter<FavoriteTVRecyclerViewAdapter.MyViewHolder>() {

    interface IFavoriteTVRecyclerViewAdapterCallback {
        fun onAdapterDeleteButtonPressed(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovie = dataList[position]
        holder.itemView.item_text_movie_title.text = currentMovie.title
        holder.itemView.item_text_movie_rating.text = currentMovie.rating
        val url = "https://image.tmdb.org/t/p/w185"
        if(currentMovie.posterPath != null) {
            Glide.with(holder.itemView)
                .load(url + currentMovie.posterPath)
                .apply(RequestOptions().override(200, 300))
                .into(holder.itemView.item_image_movie_poster)
        }
        if(currentMovie.overviewEnglish == ""){
            val text = holder.itemView.context.getString(R.string.overview_not_available_en)
            holder.itemView.item_text_movie_overview.text = text
        } else {
            holder.itemView.item_text_movie_overview.text = currentMovie.overviewEnglish
        }
        holder.itemView.item_text_movie_genre.text = currentMovie.genreEnglish
        holder.itemView.item_delete_fab.setOnClickListener {
            callback.onAdapterDeleteButtonPressed(holder.adapterPosition)
        }
        holder.itemView.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragmentFavorite(Constant.TV_FILM_TYPE).setId(currentMovie.id)
            holder.itemView.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}