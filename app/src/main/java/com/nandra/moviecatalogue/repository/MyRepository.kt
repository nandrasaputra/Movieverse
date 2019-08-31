package com.nandra.moviecatalogue.repository

import android.app.Application
import com.nandra.moviecatalogue.network.*
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val discoverService = TheMovieDBApiService(interceptor)
    private val genreService = TheMovieDBGenreApiService(interceptor)

    suspend fun fetchMovieResponse() : Response<DiscoverResponse> {
        return discoverService.getMovie("en-US")
    }

    suspend fun fetchTVSeriesResponse() : Response<DiscoverResponse> {
        return discoverService.getTVSeries("en-US")
    }

    suspend fun fetchTVGenreResponse() : Response<GenreResponse> {
        return genreService.getTVGenre("en-US")
    }

    suspend fun fetchMovieGenreResponse() : Response<GenreResponse> {
        return genreService.getMovieGenre("en-US")
    }
}