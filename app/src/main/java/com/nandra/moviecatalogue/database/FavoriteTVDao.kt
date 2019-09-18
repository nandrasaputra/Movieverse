package com.nandra.moviecatalogue.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteTVDao {
    @Query("SELECT * FROM favorite_tv")
    fun getFavoriteTVList() : LiveData<List<FavoriteTV>>

    @Query("SELECT * FROM favorite_tv WHERE id = :tvId")
    suspend fun getFavoriteTV(tvId: String) : FavoriteTV?

    @Delete
    suspend fun deleteFavoriteTV(tv: FavoriteTV)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertToFavoriteTV(tv: FavoriteTV)
}