package com.endiar.movieverse.core.data.source.local.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.endiar.movieverse.core.data.source.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movie")
    fun getFavoriteMovieList() : Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movie")
    fun getFavoriteMovieListCursor() : Cursor

    @Query("SELECT * FROM favorite_movie WHERE movieId = :movieId")
    fun getFavoriteMovie(movieId: Int) : Flow<FavoriteMovieEntity?>

    @Query("DELETE FROM favorite_movie WHERE movieId = :movieId")
    suspend fun deleteFavoriteMovie(movieId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavoriteMovie(movie: FavoriteMovieEntity)
}