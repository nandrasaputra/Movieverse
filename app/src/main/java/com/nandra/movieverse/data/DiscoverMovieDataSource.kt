package com.nandra.movieverse.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.network.apiservice.TMDBDiscoverApiService
import com.nandra.movieverse.util.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverMovieDataSource(
    private val scope: CoroutineScope,
    private val api: TMDBDiscoverApiService
) : PageKeyedDataSource<Int, Film>() {

    val networkState = MutableLiveData<NetworkState>()
    val isInitialLoaded = MutableLiveData<Boolean>()

    private var retry: (() -> Any)? = null

    fun commitRetry() {
        val previousRetry = retry
        retry = null
        previousRetry?.invoke()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Film>) {
        scope.launch(Dispatchers.IO) {
            isInitialLoaded.postValue(false)
            networkState.postValue(NetworkState.LOADING)
            try {
                val response = api.getMovie("en-US", "popularity.desc", 1)
                val totalPage = response.body()?.totalPages!!
                val nextKey =  if (totalPage > 1) {
                    2
                } else {
                    null
                }
                if (response.isSuccessful) {
                    val data: MutableList<Film> = response.body()?.results!!.toMutableList()
                    callback.onResult(data, null, nextKey)
                    isInitialLoaded.postValue(true)
                    networkState.postValue(NetworkState.LOADED)
                } else {
                    networkState.postValue(NetworkState.FAILED)
                    retry = {loadInitial(params, callback)}
                }
            } catch (exception: Exception) {
                networkState.postValue(NetworkState.FAILED)
                retry = {loadInitial(params, callback)}
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        scope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.LOADING)
            try {
                val response = api.getMovie("en-US", "popularity.desc", params.key)
                if (response.isSuccessful) {
                    val totalPage = response.body()?.totalPages!!
                    val data: MutableList<Film> = response.body()?.results!!.toMutableList()
                    val nextKey = if (params.key < totalPage) {
                        params.key + 1
                    } else {
                        null
                    }
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(data, nextKey)
                } else {
                    networkState.postValue(NetworkState.FAILED)
                    retry = {loadAfter(params, callback)}
                }
            } catch (exception: Exception) {
                networkState.postValue(NetworkState.SERVER_ERROR)
                retry = {loadAfter(params, callback)}
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {}

}