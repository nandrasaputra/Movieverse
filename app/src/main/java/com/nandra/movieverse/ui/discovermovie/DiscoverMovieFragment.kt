package com.nandra.movieverse.ui.discovermovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverPagedListAdapter
import com.nandra.movieverse.ui.discover.DiscoverSharedViewModel
import com.nandra.movieverse.util.FilmType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_discover_movie.*

@AndroidEntryPoint
class DiscoverMovieFragment : Fragment() {

    private lateinit var discoverMoviePagedListAdapter: DiscoverPagedListAdapter
    private val discoverSharedViewModel: DiscoverSharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeView()
        setupView()
    }

    private fun setupView() {
        discoverMoviePagedListAdapter = DiscoverPagedListAdapter(FilmType.FilmTypeMovie) {
            discoverSharedViewModel.retryLoadMovie()
        }

        discover_movie_recyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = discoverMoviePagedListAdapter
        }
    }

    private fun observeView() {
        discoverSharedViewModel.discoverMoviePagingLiveData.observe(viewLifecycleOwner) {
            discoverMoviePagedListAdapter.submitList(it)
        }
        discoverSharedViewModel.discoverMovieNetworkState.observe(viewLifecycleOwner) {
            discoverMoviePagedListAdapter.setNetworkState(it)
        }
    }
}