package com.nandra.moviecatalogue.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "b035113688699fc69bf05ff16f32aa80"
// https://api.themoviedb.org/3/discover/movie?api_key=b035113688699fc69bf05ff16f32aa80&language=en-US
interface TheMovieDBApiService {

    @GET("movie")
    suspend fun getMovie(
        @Query("language") language: String
    ) : Response<DiscoverMovieResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor) : TheMovieDBApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
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
                .baseUrl("https://api.themoviedb.org/3/discover/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheMovieDBApiService::class.java)
        }
    }
}