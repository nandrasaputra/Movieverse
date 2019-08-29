package com.nandra.moviecatalogue.repository

import android.app.Application
import com.nandra.moviecatalogue.data.ConnectivityInterceptor
import com.nandra.moviecatalogue.data.DiscoverMovieResponse
import com.nandra.moviecatalogue.data.TheMovieDBApiService
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val service = TheMovieDBApiService(interceptor)

    suspend fun fetchMovieResponse() : Response<DiscoverMovieResponse> {
        return service.getMovie("en-US")
    }
}