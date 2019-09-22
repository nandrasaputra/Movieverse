package com.nandra.movieverse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class, FavoriteTV::class], version = 1)
abstract class MovieverseDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun favoriteTVDao(): FavoriteTVDao

    companion object {
        @Volatile
        private var instance: MovieverseDatabase? = null

        fun getInstance(context: Context): MovieverseDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): MovieverseDatabase {
            return Room.databaseBuilder(
                context,
                MovieverseDatabase::class.java,
                "movieverse_database"
            ).build()
        }
    }
}
