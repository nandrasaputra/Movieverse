package com.nandra.movieverse.data

import androidx.paging.PageKeyedDataSource
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.repository.MyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverTVDataSource(
    private val scope: CoroutineScope,
    private val repository: MyRepository
) : PageKeyedDataSource<Int, Film>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Film>) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = repository.fetchDiscoverTVSeriesResponse()
                val totalPage = response.body()?.totalPages!!
                val nextKey =  if (totalPage > 1) {
                    2
                } else {
                    null
                }
                if (response.isSuccessful) {
                    val data: MutableList<Film> = response.body()?.results!!.toMutableList()
                    callback.onResult(data, null, nextKey)
                } else {

                }
            } catch (exception: Exception) {

            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = repository.fetchDiscoverTVSeriesResponse(params.key)
                if (response.isSuccessful) {
                    val totalPage = response.body()?.totalPages!!
                    val data: MutableList<Film> = response.body()?.results!!.toMutableList()
                    val nextKey = if (params.key < totalPage) {
                        params.key + 1
                    } else {
                        null
                    }
                    callback.onResult(data, nextKey)
                } else {

                }
            } catch (exception: Exception) {

            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = repository.fetchDiscoverTVSeriesResponse(params.key)
                if (response.isSuccessful) {
                    val data: MutableList<Film> = response.body()?.results!!.toMutableList()
                    val nextKey = if (params.key > 1) {
                        params.key - 1
                    } else {
                        null
                    }
                    callback.onResult(data, nextKey)
                } else {

                }
            } catch (exception: Exception) {

            }
        }
    }

}