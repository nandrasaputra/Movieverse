package com.endiar.movieverse.core.domain.usecase

import com.endiar.movieverse.core.domain.model.FilmDetail
import com.endiar.movieverse.core.domain.model.FilmFavoriteMovie
import com.endiar.movieverse.core.domain.model.FilmFavoriteTV
import com.endiar.movieverse.core.domain.repository.IMovieverseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalInteractor @Inject constructor(private val repository: IMovieverseRepository) : LocalUseCase {
    override fun checkFavoriteTV(tvId: Int): Flow<Boolean> = repository.checkFavoriteTV(tvId)
    override fun checkFavoriteMovie(movieId: Int): Flow<Boolean> = repository.checkFavoriteMovie(movieId)
    override suspend fun deleteFavoriteMovie(movieId: Int) = repository.deleteFavoriteMovie(movieId)
    override suspend fun insertToFavoriteMovie(movie: FilmDetail) = repository.insertToFavoriteMovie(movie)
    override suspend fun deleteFavoriteTV(tvId: Int) = repository.deleteFavoriteTV(tvId)
    override suspend fun insertToFavoriteTV(tv: FilmDetail) = repository.insertToFavoriteTV(tv)
    override fun getFavoriteMovieList(): Flow<List<FilmFavoriteMovie>> = repository.getFavoriteMovieList()
    override fun getFavoriteTVList(): Flow<List<FilmFavoriteTV>> = repository.getFavoriteTVList()
}