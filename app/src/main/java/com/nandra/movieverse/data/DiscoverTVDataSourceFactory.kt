package com.nandra.movieverse.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.network.apiservice.TMDBDiscoverApiService
import kotlinx.coroutines.CoroutineScope

class DiscoverTVDataSourceFactory(
    private val scope: CoroutineScope,
    private val api: TMDBDiscoverApiService
) : DataSource.Factory<Int, Film>() {
    val sourceLiveData = MutableLiveData<DiscoverTVDataSource>()
    override fun create(): DataSource<Int, Film> {
        val source = DiscoverTVDataSource(scope, api)
        sourceLiveData.postValue(source)
        return source
    }
}