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

        val film: Film? = intent.getParcelableExtra(EXTRA_MOVIE)
        prepareView(film!!)

        //Back Button On ActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun prepareView(film: Film) {
        detail_text_movie_title.text = film.title
        detail_text_movie_genre.text = film.genre
        detail_text_movie_rating.text = film.rating
        detail_text_movie_overview.text = film.overview
        Glide.with(this)
            .load(film.poster)
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
