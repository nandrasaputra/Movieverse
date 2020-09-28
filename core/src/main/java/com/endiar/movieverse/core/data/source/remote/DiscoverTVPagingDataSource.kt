package com.endiar.movieverse.core.data.source.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.endiar.movieverse.core.data.source.remote.network.TMDBApiService
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.utils.NetworkState
import com.endiar.movieverse.core.utils.mapFilmGistResponseToListFilmGist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverTVPagingDataSource(
    private val tmdbApiService: TMDBApiService,
    private val coroutineScope: CoroutineScope
) : PageKeyedDataSource<Int, FilmGist>() {

    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, FilmGist>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.LOADING)
            initialLoad.postValue(NetworkState.LOADING)
            try {
                val response = tmdbApiService.getDiscoverTVSeries("en-US", "popularity.desc", 1)
                val totalPage = response.totalPages
                val nextKey = if (totalPage > 1) 2 else null
                val data = mapFilmGistResponseToListFilmGist(response)
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(data, null, nextKey)
            } catch (exception: Exception) {
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(exception.message ?: "unknown error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        }
    }

    // ignored, since we only ever append to our initial load
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FilmGist>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FilmGist>) {
        coroutineScope.launch {
            networkState.postValue(NetworkState.LOADING)
            try {
                val response = tmdbApiService.getDiscoverTVSeries("en-US", "popularity.desc", params.key)
                val totalPage = response.totalPages
                val nextKey = if (params.key < totalPage) (params.key + 1) else null
                val data = mapFilmGistResponseToListFilmGist(response)
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(data, nextKey)
            } catch (exception: Exception) {
                retry = {
                    loadAfter(params, callback)
                }
                val error = NetworkState.error(exception.message ?: "unknown error")
                networkState.postValue(error)
            }
        }
    }

    fun retryAllFailed() {
        val previousRetry = retry
        retry = null
        previousRetry?.invoke()
    }

}