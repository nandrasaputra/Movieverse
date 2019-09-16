package com.nandra.moviecatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.database.FavoriteMovie
import com.nandra.moviecatalogue.util.Constant
import kotlinx.android.synthetic.main.item_favorite_list.view.*

class FavoriteMovieRecyclerViewAdapter(
    private var dataList : List<FavoriteMovie>,
    private var currentLanguage: String
) : RecyclerView.Adapter<FavoriteMovieRecyclerViewAdapter.MyViewHolder>() {

    fun changeFavoriteMovieList(newList: List<FavoriteMovie>) {
        dataList = newList
        notifyDataSetChanged()
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
        Glide.with(holder.itemView)
            .load(url + currentMovie.posterPath)
            .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
            .into(holder.itemView.item_image_movie_poster)
        if(currentLanguage == Constant.LANGUAGE_ENGLISH_VALUE) {
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
        holder.itemView.setOnClickListener {
            //TODO val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(filmType).setPosition(position)
            //TODO holder.itemView.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}