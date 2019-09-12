package com.nandra.moviecatalogue.database

import androidx.room.*

@Dao
interface FavoriteTVDao {
    @Query("SELECT * FROM favorite_tv")
    fun getAllFavoriteTV() : List<FavoriteTV>

    @Delete
    fun deleteFavoriteTV(tv: FavoriteTV)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertToFavoriteTV(tv: FavoriteTV)
}