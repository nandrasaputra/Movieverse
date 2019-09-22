package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.movieverse.R
import com.nandra.movieverse.database.FavoriteTV
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.item_favorite_list.view.*

class FavoriteTVRecyclerViewAdapter(
    private var dataList : List<FavoriteTV>,
    private var currentLanguage : String,
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
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(holder.itemView.item_image_movie_poster)
        }
        if(currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE){
            if(currentMovie.overviewEnglish == ""){
                val text = holder.itemView.context.getString(R.string.overview_not_available_en)
                holder.itemView.item_text_movie_overview.text = text
            } else {
                holder.itemView.item_text_movie_overview.text = currentMovie.overviewEnglish
            }
            holder.itemView.item_text_movie_genre.text = currentMovie.genreEnglish
        } else {
            if(currentMovie.overviewIndonesia == ""){
                val text = holder.itemView.context.getString(R.string.overview_not_available_id)
                holder.itemView.item_text_movie_overview.text = text
            } else {
                holder.itemView.item_text_movie_overview.text = currentMovie.overviewIndonesia
            }
            holder.itemView.item_text_movie_genre.text = currentMovie.genreIndonesia
        }
        holder.itemView.item_delete_fab.setOnClickListener {
            callback.onAdapterDeleteButtonPressed(holder.adapterPosition)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}