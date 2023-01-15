package com.endiar.movieverse.core.di

import com.endiar.movieverse.core.data.source.remote.DiscoverMoviePagingDataSourceFactory
import com.endiar.movieverse.core.data.source.remote.DiscoverTVPagingDataSourceFactory
import com.endiar.movieverse.core.data.source.remote.network.TMDBApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class PagingModule {

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }

    @Provides
    fun provideDiscoverMoviePagingDataSourceFactory(
        tmdbApiService: TMDBApiService,
        coroutineScope: CoroutineScope
    ): DiscoverMoviePagingDataSourceFactory {
        return DiscoverMoviePagingDataSourceFactory(tmdbApiService, coroutineScope)
    }

    @Provides
    fun provideDiscoverTVPagingDataSourceFactory(
        tmdbApiService: TMDBApiService,
        coroutineScope: CoroutineScope
    ): DiscoverTVPagingDataSourceFactory {
        return DiscoverTVPagingDataSourceFactory(tmdbApiService, coroutineScope)
    }
}