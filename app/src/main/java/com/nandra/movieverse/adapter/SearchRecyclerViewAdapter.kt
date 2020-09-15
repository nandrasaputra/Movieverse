package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.endiar.movieverse.core.domain.model.FilmSearch
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.search.SearchFragmentDirections
import com.nandra.movieverse.util.FilmType
import kotlinx.android.synthetic.main.item_search.view.*

class SearchRecyclerViewAdapter(
    private val filmType: FilmType
) : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {

    private var searchResult: List<FilmSearch> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentFilm = searchResult[position]
        when(filmType) {
            is FilmType.FilmTypeMovie -> {
                bindMovieViewProperties(holder, currentFilm)
            }
            is FilmType.FilmTypeTV -> {
                bindTVViewProperties(holder, currentFilm)
            }
        }
    }

    private fun bindMovieViewProperties(holder: SearchViewHolder, currentFilm: FilmSearch) {
        if (currentFilm.posterImagePath.isNotEmpty()) {
            val url = "https://image.tmdb.org/t/p/w185"
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterImagePath)
                .into(holder.itemView.item_search_poster)
        }
        holder.itemView.apply {
            val rating = "${currentFilm.voteAverage} / 10"
            item_search_title.text = currentFilm.movieTitle
            item_search_rating.text = rating
            item_search_released_date.text = currentFilm.releaseDate
        }
        holder.itemView.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragmentInDiscover(filmType.typeValue, currentFilm.id)
            holder.itemView.findNavController().navigate(action)
        }
    }

    private fun bindTVViewProperties(holder: SearchViewHolder, currentFilm: FilmSearch) {
        if (currentFilm.posterImagePath.isNotEmpty()) {
            val url = "https://image.tmdb.org/t/p/w185"
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterImagePath)
                .into(holder.itemView.item_search_poster)
        }
        holder.itemView.apply {
            val rating = "${currentFilm.voteAverage} / 10"
            item_search_title.text = currentFilm.tvTitle
            item_search_rating.text = rating
            item_search_released_date.text = currentFilm.firstAirDate
        }
        holder.itemView.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragmentInDiscover(filmType.typeValue, currentFilm.id)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun submitList(newList: List<FilmSearch>?) {
        newList?.let {
            searchResult = it
            notifyDataSetChanged()
        }
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
