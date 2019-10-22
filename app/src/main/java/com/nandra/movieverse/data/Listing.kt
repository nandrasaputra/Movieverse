package com.nandra.movieverse.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nandra.movieverse.util.NetworkState

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val initialState: LiveData<Boolean>,
    val retry: () -> Unit)