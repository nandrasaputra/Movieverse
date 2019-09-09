package com.nandra.moviecatalogue.repository

import android.app.Application
import com.nandra.moviecatalogue.network.ConnectivityInterceptor
import com.nandra.moviecatalogue.network.DetailResponse
import com.nandra.moviecatalogue.network.DiscoverResponse
import com.nandra.moviecatalogue.network.GenreResponse
import com.nandra.moviecatalogue.network.apiservice.TheMovieDBDetailApiService
import com.nandra.moviecatalogue.network.apiservice.TheMovieDBDiscoverApiService
import com.nandra.moviecatalogue.network.apiservice.TheMovieDBGenreApiService
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val discoverService =
        TheMovieDBDiscoverApiService(interceptor)
    private val genreService =
        TheMovieDBGenreApiService(interceptor)
    private val detailService =
        TheMovieDBDetailApiService(interceptor)

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

    suspend fun fetchMovieDetailResponse(id: String, parameter: String = "videos,credits") : Response<DetailResponse> {
        return detailService.getMovieDetail(id, parameter)
    }

    suspend fun fetchTVDetailResponse(id: String, parameter: String = "videos,credits") : Response<DetailResponse> {
        return detailService.getTVDetail(id, parameter)
    }
}