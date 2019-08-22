package com.nandra.moviecatalogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedImageView

class MainMovieListAdapter(val context: Context, val movies : ArrayList<Movie>) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        if(view == null) {
            val mView = LayoutInflater.from(context).inflate(R.layout.main_movie_list_item, viewGroup, false)
            val mViewHolder = MovieViewHolder(mView)
            val movie = getItem(position) as Movie
            mViewHolder.bind(movie)
            return mView
        } else {
            val mViewHolder = MovieViewHolder(view)
            val movie = getItem(position) as Movie
            mViewHolder.bind(movie)
            return view
        }

    }

    override fun getItem(i: Int): Any {
        return movies[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(view: View) {
        private val title : TextView = view.findViewById(R.id.item_title_placeholder)
        private val rating : TextView = view.findViewById(R.id.item_rating_placeholder)
        private val genre : TextView = view.findViewById(R.id.item_genre_placeholder)
        private val img : RoundedImageView = view.findViewById(R.id.item_poster_placeholder)
        private val overview : TextView = view.findViewById(R.id.item_overview)

        fun bind(movie: Movie){
            title.text = movie.title
            rating.text = movie.rating
            genre.text = movie.genre
            img.setImageResource(movie.img)
            overview.text = movie.overview
        }
    }
}