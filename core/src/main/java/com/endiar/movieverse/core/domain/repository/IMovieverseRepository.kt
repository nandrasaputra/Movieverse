package com.endiar.movieverse.core.domain.repository

import com.endiar.movieverse.core.data.DiscoverListing
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IMovieverseRepository {

    fun getTrendingFilm(): Flow<Resource<List<FilmGist>>>
    fun getNowPlayingFilm(): Flow<Resource<List<FilmGist>>>
    fun getDetailMovie(movieId: Int): Flow<Resource<FilmDetail>>
    fun getDetailTV(tvId: Int): Flow<Resource<FilmDetail>>

    fun checkFavoriteMovie(movieId: Int): Flow<Boolean>
    suspend fun deleteFavoriteMovie(movieId: Int)
    suspend fun insertToFavoriteMovie(movie: FilmDetail)

    fun checkFavoriteTV(tvId: Int): Flow<Boolean>
    suspend fun deleteFavoriteTV(tvId: Int)
    suspend fun insertToFavoriteTV(tv: FilmDetail)

    fun getFavoriteMovieList(): Flow<List<FilmFavoriteMovie>>
    fun getFavoriteTVList(): Flow<List<FilmFavoriteTV>>

    fun getDiscoverMovieData(): DiscoverListing<FilmGist>
    fun getDiscoverTVData(): DiscoverListing<FilmGist>

    fun searchMovie(query: String): Flow<Resource<List<FilmSearch>>>
    fun searchTV(query: String): Flow<Resource<List<FilmSearch>>>
}