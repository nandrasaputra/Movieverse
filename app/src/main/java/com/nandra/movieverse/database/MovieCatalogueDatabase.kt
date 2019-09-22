package com.nandra.movieverse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class, FavoriteTV::class], version = 1)
abstract class MovieCatalogueDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun favoriteTVDao(): FavoriteTVDao

    companion object {
        @Volatile
        private var instance: MovieCatalogueDatabase? = null

        fun getInstance(context: Context): MovieCatalogueDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): MovieCatalogueDatabase {
            return Room.databaseBuilder(
                context,
                MovieCatalogueDatabase::class.java,
                "movie_catalogue_database"
            ).build()
        }
    }
}
