package com.nandra.movieverse.ui.favoritemovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endiar.movieverse.core.domain.model.FilmFavoriteMovie
import com.endiar.movieverse.core.utils.Constant
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.FavoriteFilmRecyclerViewAdapter
import com.nandra.movieverse.ui.favorite.FavoriteFragmentDirections
import com.nandra.movieverse.ui.favorite.FavoriteSharedViewModel
import com.nandra.movieverse.util.setVisibilityGone
import com.nandra.movieverse.util.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.favorite_film.*

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment() {

    private val favoriteSharedViewModel: FavoriteSharedViewModel by activityViewModels()
    private lateinit var favoriteFilmAdapter: FavoriteFilmRecyclerViewAdapter<FilmFavoriteMovie>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupView()
    }

    private fun observeData() {
        favoriteSharedViewModel.favoriteMovieMediatorLiveData.observe(viewLifecycleOwner) { favoriteList ->
            favoriteList?.let {
                favoriteFilmAdapter.submitList(it)

                if (it.isNotEmpty()) {
                    favorite_film_no_item_lottie.setVisibilityGone()
                } else {
                    favorite_film_no_item_lottie.setVisibilityVisible()
                }
            }
        }
    }

    private fun setupView() {
        favoriteFilmAdapter = FavoriteFilmRecyclerViewAdapter(
            { favoriteSharedViewModel.removeFavoriteMovie(it) },
            {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragmentInFavorite(Constant.MOVIE_FILM_TYPE, it)
                findNavController().navigate(action)
            }
        )

        favorite_film_recyclerview.apply {
            adapter = favoriteFilmAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}