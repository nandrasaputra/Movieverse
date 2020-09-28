package com.nandra.movieverse.ui.discovertv

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
import kotlinx.android.synthetic.main.fragment_discover_tv.*

@AndroidEntryPoint
class DiscoverTVFragment : Fragment() {

    private lateinit var discoverTVPagedListAdapter: DiscoverPagedListAdapter
    private val discoverSharedViewModel: DiscoverSharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover_tv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeView()
        setupView()
    }

    private fun setupView() {

        discoverTVPagedListAdapter = DiscoverPagedListAdapter(FilmType.FilmTypeTV) {
            discoverSharedViewModel.retryLoadTV()
        }

        discover_tv_recyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = discoverTVPagedListAdapter
        }
    }

    private fun observeView() {
        discoverSharedViewModel.discoverTVPagingLiveData.observe(viewLifecycleOwner) {
            discoverTVPagedListAdapter.submitList(it)
        }
        discoverSharedViewModel.discoverTVNetworkState.observe(viewLifecycleOwner) {
            discoverTVPagedListAdapter.setNetworkState(it)
        }
    }
}