package com.endiar.movieverse.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.endiar.movieverse.core.data.source.local.entity.FavoriteMovieEntity
import com.endiar.movieverse.core.data.source.local.entity.FavoriteTVEntity

@Database(entities = [FavoriteMovieEntity::class, FavoriteTVEntity::class], version = 1, exportSchema = false)
abstract class MovieverseDatabase  : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun favoriteTvDao(): FavoriteTvDao

}