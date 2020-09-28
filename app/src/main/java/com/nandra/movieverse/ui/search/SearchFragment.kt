package com.nandra.movieverse.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.endiar.movieverse.core.data.Resource
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.SearchRecyclerViewAdapter
import com.nandra.movieverse.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private val searchViewModel: SearchViewModel by viewModels()
    private val args: SearchFragmentArgs by navArgs()
    private lateinit var filmType: FilmType
    private lateinit var searchAdapter: SearchRecyclerViewAdapter
    private var searchJob: Job? = null
    private var isInSearchState: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupView()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            performSearch(it)
            hideKeyboardFrom(requireContext(), requireView())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            performSearch(it)
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        hideKeyboardFrom(requireContext(), requireView())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupView() {

        filmType = getFilmTypeFromString(args.mediaType)
        searchAdapter = SearchRecyclerViewAdapter(filmType)

        fragment_search_recycler_view.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setOnTouchListener { _, _ ->
                hideKeyboardFrom(requireContext(), requireView())
                return@setOnTouchListener false
            }
        }

        fragment_search_searchview.setOnQueryTextListener(this)

        when(filmType) {
            is FilmType.FilmTypeMovie -> {
                fragment_search_searchview.queryHint = "Search Movie"
            }
            is FilmType.FilmTypeTV -> {
                fragment_search_searchview.queryHint = "Search TV"
            }
        }

        fragment_search_toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_toolbar)
            setNavigationOnClickListener { activity?.onBackPressed() }
        }

        fragment_search_searchview.requestFocus()
        showKeyboardFrom(requireContext())
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            setSearchState(true)
            searchJob?.run {
                if (this.isActive) {
                    this.cancel()
                }
            }
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(800)
                when(filmType) {
                    is FilmType.FilmTypeMovie -> {
                        searchViewModel.searchMovie(query)
                    }
                    is FilmType.FilmTypeTV -> {
                        searchViewModel.searchTV(query)
                    }
                }
            }
        } else {
            setSearchState(false)
            searchViewModel.resetSearch()
        }
    }

    private fun observeData() {
        searchViewModel.searchMediatorLiveData.observe(viewLifecycleOwner) { resource ->
            if (resource != null && isInSearchState) {
                when(resource) {
                    is Resource.Success -> {
                        fragment_search_progress_bar.setVisibilityGone()
                        searchAdapter.submitList(resource.data)
                    }
                    is Resource.Loading -> {
                        fragment_search_progress_bar.setVisibilityVisible()
                    }
                    is Resource.Error -> {
                        fragment_search_progress_bar.setVisibilityGone()
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setSearchState(boolean: Boolean) {
        if (boolean) {
            isInSearchState = true
            fragment_search_cover_group.setVisibilityGone()
            fragment_search_recycler_view.setVisibilityVisible()
        } else {
            isInSearchState = false
            fragment_search_cover_group.setVisibilityVisible()
            fragment_search_recycler_view.setVisibilityGone()
        }
    }
}