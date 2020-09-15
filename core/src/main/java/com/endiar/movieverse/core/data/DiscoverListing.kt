package com.endiar.movieverse.core.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.endiar.movieverse.core.utils.NetworkState

data class DiscoverListing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)