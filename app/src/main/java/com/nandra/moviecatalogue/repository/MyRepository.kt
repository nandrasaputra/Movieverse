package com.nandra.moviecatalogue.repository

import android.app.Application
import com.nandra.moviecatalogue.network.ConnectivityInterceptor
import com.nandra.moviecatalogue.network.apiservice.TheMovieDBDetailApiService
import com.nandra.moviecatalogue.network.apiservice.TheMovieDBDiscoverApiService
import com.nandra.moviecatalogue.network.apiservice.YandexTranslationApiService
import com.nandra.moviecatalogue.network.response.DetailResponse
import com.nandra.moviecatalogue.network.response.DiscoverResponse
import com.nandra.moviecatalogue.network.response.YandexResponse
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val discoverService =
        TheMovieDBDiscoverApiService(interceptor)
    private val detailService =
        TheMovieDBDetailApiService(interceptor)
    private val yandexService =
        YandexTranslationApiService(interceptor)

    suspend fun fetchDiscoverMovieResponse() : Response<DiscoverResponse> {
        return discoverService.getMovie("en-US")
    }

    suspend fun fetchDiscoverTVSeriesResponse() : Response<DiscoverResponse> {
        return discoverService.getTVSeries("en-US")
    }

    suspend fun fetchMovieDetailResponse(id: String, parameter: String = "videos,credits") : Response<DetailResponse> {
        return detailService.getMovieDetail(id, parameter)
    }

    suspend fun fetchTVDetailResponse(id: String, parameter: String = "videos,credits") : Response<DetailResponse> {
        return detailService.getTVDetail(id, parameter)
    }

    suspend fun translateText(text: List<String>) : Response<YandexResponse> {
        return yandexService.translateText("en-id", text)
    }
}