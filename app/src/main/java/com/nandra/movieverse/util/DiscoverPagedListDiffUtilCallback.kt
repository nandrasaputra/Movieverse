package com.nandra.movieverse.util

import androidx.recyclerview.widget.DiffUtil
import com.endiar.movieverse.core.domain.model.FilmGist

class DiscoverPagedListDiffUtilCallback(private val type: FilmType): DiffUtil.ItemCallback<FilmGist>() {

    override fun areItemsTheSame(oldItem: FilmGist, newItem: FilmGist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FilmGist, newItem: FilmGist): Boolean {
        return when (type) {
            is FilmType.FilmTypeMovie -> {oldItem.movieTitle == newItem.movieTitle}
            is FilmType.FilmTypeTV -> { oldItem.tvTitle == newItem.tvTitle }
        }
    }
}