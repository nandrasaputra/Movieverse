package com.nandra.moviecatalogue.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.ViewModel.SharedViewModel
import com.nandra.moviecatalogue.adapter.RecyclerViewAdapter
import com.nandra.moviecatalogue.network.Film
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.*

class MovieFragment : Fragment() {

    private var moviesList: ArrayList<Film> = arrayListOf()
    private lateinit var movieRecyclerView : RecyclerView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var filmType: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        movieRecyclerView = view.findViewById(R.id.movie_recyclerview)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.isLoading.observe(this, Observer {
            loadingIndicator(it)
        })
        sharedViewModel.isError.observe(this, Observer {
            errorIndicator(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filmType = getString(R.string.film_type_movie)
        attemptPrepareView()
        movieRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadingIndicator(state: Boolean) {
        if (state) {
            movie_loading_back.visibility = View.VISIBLE
        }
        else {
            movie_loading_back.visibility = View.GONE
        }
    }

    private fun errorIndicator(state: Boolean){
        if(state){
            movie_error_back.visibility = View.VISIBLE
            movie_error_button.setOnClickListener {
                prepareMovieListView()
            }
        } else {
            movie_error_back.visibility = View.GONE
        }
    }

    private fun attemptPrepareView() {
        if(sharedViewModel.isError.value == true) {
            errorIndicator(sharedViewModel.isError.value!!)
            return
        }
        prepareMovieListView()
    }

    private fun prepareMovieListView() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        Glide.with(this)
            .load(R.drawable.img_loading_indicator)
            .into(movie_loading_image)
        scope.launch {
            val task = async {
                sharedViewModel.getListMovie()
            }
            moviesList = task.await()
            movieRecyclerView.adapter = RecyclerViewAdapter(moviesList, filmType)
        }
    }
}