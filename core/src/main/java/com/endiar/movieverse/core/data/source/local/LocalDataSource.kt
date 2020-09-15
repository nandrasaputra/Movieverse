package com.endiar.movieverse.core.data.source.local

import com.endiar.movieverse.core.data.source.local.entity.FavoriteMovieEntity
import com.endiar.movieverse.core.data.source.local.entity.FavoriteTVEntity
import com.endiar.movieverse.core.data.source.local.room.FavoriteMovieDao
import com.endiar.movieverse.core.data.source.local.room.FavoriteTvDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val favoriteTvDao: FavoriteTvDao
) {
    // Movie
    fun getFavoriteMovieList() : Flow<List<FavoriteMovieEntity>> = favoriteMovieDao.getFavoriteMovieList()
    fun getFavoriteMovie(movieId: Int) : Flow<FavoriteMovieEntity?> = favoriteMovieDao.getFavoriteMovie(movieId)
    suspend fun deleteFavoriteMovie(movieId: Int) = favoriteMovieDao.deleteFavoriteMovie(movieId)
    suspend fun insertToFavoriteMovie(movie: FavoriteMovieEntity) = favoriteMovieDao.insertToFavoriteMovie(movie)
    // TV
    fun getFavoriteTVList() : Flow<List<FavoriteTVEntity>> = favoriteTvDao.getFavoriteTVList()
    fun getFavoriteTV(tvId: Int) : Flow<FavoriteTVEntity?> = favoriteTvDao.getFavoriteTV(tvId)
    suspend fun deleteFavoriteTV(tvId: Int) = favoriteTvDao.deleteFavoriteTV(tvId)
    suspend fun insertToFavoriteTV(tv: FavoriteTVEntity) = favoriteTvDao.insertToFavoriteTV(tv)
}