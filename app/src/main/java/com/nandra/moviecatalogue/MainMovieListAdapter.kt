package com.nandra.moviecatalogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView

class MainMovieListAdapter(private val context: Context, private val movies : ArrayList<Movie>) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        return if(view == null) {
            val newView = LayoutInflater.from(context).inflate(R.layout.item_main_movie_list, viewGroup, false)
            val mViewHolder = MovieViewHolder(newView)
            val movie = getItem(position) as Movie
            mViewHolder.bind(movie)
            newView
        } else {
            val mViewHolder = MovieViewHolder(view)
            val movie = getItem(position) as Movie
            mViewHolder.bind(movie)
            view
        }
    }

    override fun getItem(position: Int): Any {
        return movies[position]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(view: View) {
        private val title : TextView = view.findViewById(R.id.item_text_movie_title)
        private val rating : TextView = view.findViewById(R.id.item_text_movie_rating)
        private val genre : TextView = view.findViewById(R.id.item_text_movie_genre)
        private val img : RoundedImageView = view.findViewById(R.id.item_image_movie_poster)
        private val overview : TextView = view.findViewById(R.id.item_text_movie_overview)

        fun bind(movie: Movie){
            title.text = movie.title
            rating.text = movie.rating
            genre.text = movie.genre
            Glide.with(context)
                .load(movie.img)
                .apply(RequestOptions().override(200, 300))     //Optimizing Image Loading For Thumbnail
                .into(img)
            overview.text = movie.overview
        }
    }
}