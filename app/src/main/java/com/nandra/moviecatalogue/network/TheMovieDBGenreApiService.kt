package com.nandra.moviecatalogue.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.themoviedb.org/3/genre/movie/list?api_key=b035113688699fc69bf05ff16f32aa80&language=en-US
//https://api.themoviedb.org/3/genre/tv/list?api_key=b035113688699fc69bf05ff16f32aa80&language=en-US
interface TheMovieDBGenreApiService {

    @GET("movie/list")
    suspend fun getMovieGenre(
        @Query("language") language: String
    ) : Response<GenreResponse>

    @GET("tv/list")
    suspend fun getTVGenre(
        @Query("language") language: String
    ) : Response<GenreResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): TheMovieDBGenreApiService {
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
                .baseUrl("https://api.themoviedb.org/3/genre/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheMovieDBGenreApiService::class.java)
        }
    }
}