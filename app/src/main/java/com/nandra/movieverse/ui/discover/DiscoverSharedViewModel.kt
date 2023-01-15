package com.nandra.movieverse.ui.discover

import androidx.lifecycle.ViewModel
import com.endiar.movieverse.core.domain.usecase.RemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel class DiscoverSharedViewModel @Inject constructor(
    remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val movieListing = remoteUseCase.getDiscoverMovie()
    private val tvListing = remoteUseCase.getDiscoverTV()

    val discoverMoviePagingLiveData = movieListing.pagedList
    val discoverMovieNetworkState = movieListing.networkState

    val discoverTVPagingLiveData = tvListing.pagedList
    val discoverTVNetworkState = tvListing.networkState

    fun retryLoadMovie() {
        movieListing.retry.invoke()
    }

    fun retryLoadTV() {
        tvListing.retry.invoke()
    }

}