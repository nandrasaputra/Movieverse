package com.nandra.movieverse.data

import androidx.recyclerview.widget.DiffUtil
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.util.Constant

class DiffUtilCallback(
    private val type: String
) : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
        return if (type == Constant.MOVIE_FILM_TYPE)
            oldItem.title == newItem.title
        else
            oldItem.tvName == newItem.tvName
    }

}