package com.nandra.moviecatalogue.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movie")
    fun getFavoriteMovieList() : LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_movie WHERE id = :movieId")
    suspend fun getFavoriteMovie(movieId: String) : FavoriteMovie?

    @Delete
    suspend fun deleteFavoriteMovie(movie: FavoriteMovie)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertToFavoriteMovie(movie: FavoriteMovie)
}