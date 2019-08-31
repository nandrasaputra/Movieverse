package com.nandra.moviecatalogue.repository

import android.app.Application
import com.nandra.moviecatalogue.network.*
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val discoverService = TheMovieDBApiService(interceptor)
    private val genreService = TheMovieDBGenreApiService(interceptor)

    suspend fun fetchMovieResponse(language: String) : Response<DiscoverResponse> {
        return discoverService.getMovie(language)
    }

    suspend fun fetchTVSeriesResponse(language: String) : Response<DiscoverResponse> {
        return discoverService.getTVSeries(language)
    }

    suspend fun fetchTVGenreResponse(language: String) : Response<GenreResponse> {
        return genreService.getTVGenre(language)
    }

    suspend fun fetchMovieGenreResponse(language: String) : Response<GenreResponse> {
        return genreService.getMovieGenre(language)
    }
}