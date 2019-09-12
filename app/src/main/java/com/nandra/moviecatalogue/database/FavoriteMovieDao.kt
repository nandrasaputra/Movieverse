package com.nandra.moviecatalogue.database

import androidx.room.*

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movie")
    fun getAllFavoriteMovie() : List<FavoriteMovie>

    @Delete
    fun deleteFavoriteMovie(movie: FavoriteMovie)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertToFavoriteMovie(movie: FavoriteMovie)
}