package com.nandra.moviecatalogue.network.apiservice

import com.nandra.moviecatalogue.network.ConnectivityInterceptor
import com.nandra.moviecatalogue.network.DetailResponse
import com.nandra.moviecatalogue.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBDetailApiService {
    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: String,
        @Query("append_to_response") parameter: String
    ) : Response<DetailResponse>

    @GET("tv/{id}")
    suspend fun getTVDetail(
        @Path("id") id: String,
        @Query("append_to_response") parameter: String
    ) : Response<DetailResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor) : TheMovieDBDetailApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", Constant.API_KEY)
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
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheMovieDBDetailApiService::class.java)
        }
    }
}