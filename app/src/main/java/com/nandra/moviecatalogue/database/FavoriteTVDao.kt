package com.nandra.moviecatalogue.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteTVDao {
    @Query("SELECT * FROM favorite_tv")
    fun getAllFavoriteTV() : LiveData<List<FavoriteTV>>

    @Delete
    suspend fun deleteFavoriteTV(tv: FavoriteTV)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertToFavoriteTV(tv: FavoriteTV)
}