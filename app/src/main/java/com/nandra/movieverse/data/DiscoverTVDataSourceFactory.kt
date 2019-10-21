package com.nandra.movieverse.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.repository.MyRepository
import kotlinx.coroutines.CoroutineScope

class DiscoverTVDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: MyRepository
) : DataSource.Factory<Int, Film>() {
    val sourceLiveData = MutableLiveData<DiscoverTVDataSource>()
    override fun create(): DataSource<Int, Film> {
        val source = DiscoverTVDataSource(scope, repository)
        sourceLiveData.postValue(source)
        return source
    }
}