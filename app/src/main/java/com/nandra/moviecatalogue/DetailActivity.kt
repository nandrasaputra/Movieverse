package com.nandra.moviecatalogue

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nandra.moviecatalogue.MainActivity.Companion.EXTRA_MOVIE
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movie: Movie = intent.getParcelableExtra(EXTRA_MOVIE)
        prepareView(movie)

        //Back Button On ActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun prepareView(movie: Movie) {
        detail_text_movie_title.text = movie.title
        detail_text_movie_genre.text = movie.genre
        detail_text_movie_rating.text = movie.rating
        detail_text_movie_overview.text = movie.overview
        Glide.with(this)
            .load(movie.poster)
            .into(detail_image_movie_poster)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
