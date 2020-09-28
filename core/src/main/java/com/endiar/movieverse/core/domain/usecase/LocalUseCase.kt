package com.endiar.movieverse.core.domain.usecase

import com.endiar.movieverse.core.domain.model.FilmDetail
import com.endiar.movieverse.core.domain.model.FilmFavoriteMovie
import com.endiar.movieverse.core.domain.model.FilmFavoriteTV
import kotlinx.coroutines.flow.Flow

interface LocalUseCase {

    fun checkFavoriteTV(tvId: Int): Flow<Boolean>
    fun checkFavoriteMovie(movieId: Int): Flow<Boolean>
    suspend fun deleteFavoriteMovie(movieId: Int)
    suspend fun insertToFavoriteMovie(movie: FilmDetail)
    suspend fun deleteFavoriteTV(tvId: Int)
    suspend fun insertToFavoriteTV(tv: FilmDetail)
    fun getFavoriteMovieList(): Flow<List<FilmFavoriteMovie>>
    fun getFavoriteTVList(): Flow<List<FilmFavoriteTV>>

}