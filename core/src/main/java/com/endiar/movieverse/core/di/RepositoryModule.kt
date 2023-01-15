package com.endiar.movieverse.core.di

import com.endiar.movieverse.core.data.MovieverseRepository
import com.endiar.movieverse.core.domain.repository.IMovieverseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(movieverseRepository: MovieverseRepository): IMovieverseRepository

}