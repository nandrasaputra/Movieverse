package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.endiar.movieverse.core.domain.model.FavoriteFilm
import com.nandra.movieverse.R
import kotlinx.android.synthetic.main.item_favorite_list.view.*

class FavoriteFilmRecyclerViewAdapter<T: FavoriteFilm>(
    private val onDeleteButtonClickCallback: (Int) -> Unit,
    private val onItemClickCallback: (Int) -> Unit
) : RecyclerView.Adapter<FavoriteFilmRecyclerViewAdapter.MyViewHolder>() {

    private var dataList : List<T> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFilm = dataList[position]
        holder.itemView.item_text_movie_title.text = currentFilm.title
        val voteAverageInPercent = ((currentFilm.voteAverage * 10).toInt()).toString() + "%"
        holder.itemView.item_text_movie_rating.text = voteAverageInPercent
        val url = "https://image.tmdb.org/t/p/w185"
        if(currentFilm.posterPath.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterPath)
                .apply(RequestOptions().override(200, 300))
                .into(holder.itemView.item_image_movie_poster)
        }
        if(currentFilm.overview.isEmpty()){
            val text = holder.itemView.context.getString(R.string.overview_not_available_en)
            holder.itemView.item_text_movie_overview.text = text
        } else {
            holder.itemView.item_text_movie_overview.text = currentFilm.overview
        }
        holder.itemView.item_text_movie_genre.text = currentFilm.genre
        holder.itemView.item_delete_fab.setOnClickListener {
            onDeleteButtonClickCallback(currentFilm.id)
        }
        holder.itemView.setOnClickListener {
            onItemClickCallback(currentFilm.id)
        }
    }

    fun submitList(newList: List<T>) {
        dataList = newList
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
