package com.nandra.movieverse.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.nandra.movieverse.data.DiscoverMovieDataSourceFactory
import com.nandra.movieverse.data.DiscoverTVDataSourceFactory
import com.nandra.movieverse.data.Listing
import com.nandra.movieverse.database.FavoriteMovie
import com.nandra.movieverse.database.FavoriteTV
import com.nandra.movieverse.database.MovieverseDatabase
import com.nandra.movieverse.network.ConnectivityInterceptor
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.network.apiservice.*
import com.nandra.movieverse.network.response.DetailResponse
import com.nandra.movieverse.network.response.DiscoverResponse
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val discoverService =
        TMDBDiscoverApiService(interceptor)
    private val detailService =
        TMDBDetailApiService(interceptor)
    private val trendingService =
        TMDBTrendingApiService(interceptor)
    private val nowPlayingService =
        TMDBNowPlayingApiService(interceptor)
    private val searchService =
        TMDBSearchApiService(interceptor)
    private val database = MovieverseDatabase.getInstance(app)

    suspend fun fetchMovieDetailResponse(id: String, parameter: String = "videos,credits,images") : Response<DetailResponse> {
        return detailService.getMovieDetail(id, parameter)
    }

    suspend fun fetchTVDetailResponse(id: String, parameter: String = "videos,credits,images") : Response<DetailResponse> {
        return detailService.getTVDetail(id, parameter)
    }

    suspend fun saveMovieToFavorite(movie: FavoriteMovie) {
        database.favoriteMovieDao().insertToFavoriteMovie(movie)
    }

    suspend fun saveTVToFavorite(tv: FavoriteTV) {
        database.favoriteTVDao().insertToFavoriteTV(tv)
    }

    fun getFavoriteMovieList() : LiveData<List<FavoriteMovie>> {
        return database.favoriteMovieDao().getFavoriteMovieList()
    }

    fun getFavoriteTVList() : LiveData<List<FavoriteTV>> {
        return database.favoriteTVDao().getFavoriteTVList()
    }

    suspend fun removeFavoriteMovie(movie: FavoriteMovie) {
        database.favoriteMovieDao().deleteFavoriteMovie(movie)
    }

    suspend fun removeFavoriteTV(tv: FavoriteTV) {
        database.favoriteTVDao().deleteFavoriteTV(tv)
    }

    suspend fun fetchTrendingList() : Response<DiscoverResponse> {
        return trendingService.getAllWeekTrending()
    }

    suspend fun fetchNowPlayingList() : Response<DiscoverResponse> {
        return nowPlayingService.getNowPlaying(1)
    }

    suspend fun searchMovie(query: String, page: Int) : Response<DiscoverResponse> {
        return searchService.searchMovie(query, page)
    }

    suspend fun searchTV(query: String, page: Int) : Response<DiscoverResponse> {
        return searchService.searchTV(query, page)
    }

    suspend fun todayReleases(date: String) : Response<DiscoverResponse> {
        return discoverService.getSpecifiedDateReleaseMovie(date, date)
    }

    fun discoverMovieData(scope: CoroutineScope): Listing<Film> {
        val sourceFactory = DiscoverMovieDataSourceFactory(scope, discoverService)
        val livePagedList = sourceFactory.toLiveData(pageSize = 30)

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {sourceFactory.sourceLiveData.value?.commitRetry()},
            initialState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.isInitialLoaded
            }
        )
    }

    fun discoverTVData(scope: CoroutineScope): Listing<Film> {
        val sourceFactory = DiscoverTVDataSourceFactory(scope, discoverService)
        val livePagedList = sourceFactory.toLiveData(pageSize = 30)

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {sourceFactory.sourceLiveData.value?.commitRetry()},
            initialState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.isInitialLoaded
            }
        )
    }
}