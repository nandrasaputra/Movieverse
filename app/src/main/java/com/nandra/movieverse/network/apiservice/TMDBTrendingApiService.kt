package com.nandra.movieverse.network.apiservice

import com.nandra.movieverse.network.ConnectivityInterceptor
import com.nandra.movieverse.network.response.DiscoverResponse
import com.nandra.movieverse.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TMDBTrendingApiService {
    @GET("all/week")
    suspend fun getAllWeekTrending() : Response<DiscoverResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor) : TMDBTrendingApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", Constant.API_KEY_MOVIE_DB)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.themoviedb.org/3/trending/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TMDBTrendingApiService::class.java)
        }
    }
}