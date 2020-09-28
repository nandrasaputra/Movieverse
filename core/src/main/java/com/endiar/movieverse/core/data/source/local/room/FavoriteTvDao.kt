package com.endiar.movieverse.core.data.source.local.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.endiar.movieverse.core.data.source.local.entity.FavoriteTVEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTvDao {
    @Query("SELECT * FROM favorite_tv")
    fun getFavoriteTVList() : Flow<List<FavoriteTVEntity>>

    @Query("SELECT * FROM favorite_tv")
    fun getFavoriteTVListCursor() : Cursor

    @Query("SELECT * FROM favorite_tv WHERE tvId = :tvId")
    fun getFavoriteTV(tvId: Int) : Flow<FavoriteTVEntity?>

    @Query("DELETE FROM favorite_tv WHERE tvId = :tvId")
    suspend fun deleteFavoriteTV(tvId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavoriteTV(tv: FavoriteTVEntity)
}