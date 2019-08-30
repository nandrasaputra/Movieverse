package com.nandra.moviecatalogue.repository

import android.app.Application
import com.nandra.moviecatalogue.network.ConnectivityInterceptor
import com.nandra.moviecatalogue.network.DiscoverResponse
import com.nandra.moviecatalogue.network.TheMovieDBApiService
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val service = TheMovieDBApiService(interceptor)

    suspend fun fetchMovieResponse() : Response<DiscoverResponse> {
        return service.getMovie("en-US")
    }

    suspend fun fetchTVSeriesResponse() : Response<DiscoverResponse> {
        return service.getTVSeries("en-US")
    }
}