package com.endiar.movieverse.core.di

import android.content.Context
import androidx.room.Room
import com.endiar.movieverse.core.data.source.local.room.FavoriteMovieDao
import com.endiar.movieverse.core.data.source.local.room.FavoriteTvDao
import com.endiar.movieverse.core.data.source.local.room.MovieverseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieverseDatabase = Room.databaseBuilder(
        context,
        MovieverseDatabase::class.java,
        "movieverse.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideFavoriteMovieDao(database: MovieverseDatabase): FavoriteMovieDao = database.favoriteMovieDao()

    @Provides
    fun provideFavoriteTvDao(database: MovieverseDatabase): FavoriteTvDao = database.favoriteTvDao()

}