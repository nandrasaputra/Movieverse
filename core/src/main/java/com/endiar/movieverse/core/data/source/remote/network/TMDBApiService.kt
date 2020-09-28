package com.endiar.movieverse.core.data.source.remote.network

import com.endiar.movieverse.core.data.source.remote.response.FilmDetailResponse
import com.endiar.movieverse.core.data.source.remote.response.FilmGistResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("append_to_response") parameter: String
    ) : FilmDetailResponse

    @GET("tv/{id}")
    suspend fun getTVDetail(
        @Path("id") id: Int,
        @Query("append_to_response") parameter: String
    ) : FilmDetailResponse

    @GET("discover/movie")
    suspend fun getDiscoverMovie(
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int
    ) : FilmGistResponse

    @GET("discover/tv")
    suspend fun getDiscoverTVSeries(
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int
    ) : FilmGistResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying (
        @Query("page") page: Int
    ) : FilmGistResponse

    @GET("search/movie")
    suspend fun searchMovie (
        @Query("query") query: String,
        @Query("page") page: Int
    ) : FilmGistResponse

    @GET("search/tv")
    suspend fun searchTV (
        @Query("query") query: String,
        @Query("page") page: Int
    ) : FilmGistResponse

    @GET("trending/all/week")
    suspend fun getAllWeekTrending() : FilmGistResponse
}