package com.endiar.movieverse.core.data.source.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.endiar.movieverse.core.data.source.remote.network.TMDBApiService
import com.endiar.movieverse.core.domain.model.FilmGist
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DiscoverTVPagingDataSourceFactory @Inject constructor(
    private val tmdbApiService: TMDBApiService,
    private val coroutineScope: CoroutineScope
) : DataSource.Factory<Int, FilmGist>() {

    val sourceLiveData = MutableLiveData<DiscoverTVPagingDataSource>()

    override fun create(): DataSource<Int, FilmGist> {
        val source = DiscoverTVPagingDataSource(tmdbApiService, coroutineScope)
        sourceLiveData.postValue(source)
        return source
    }
}