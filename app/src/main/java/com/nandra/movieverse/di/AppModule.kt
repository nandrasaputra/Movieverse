package com.nandra.movieverse.di

import com.endiar.movieverse.core.domain.usecase.LocalInteractor
import com.endiar.movieverse.core.domain.usecase.LocalUseCase
import com.endiar.movieverse.core.domain.usecase.RemoteInteractor
import com.endiar.movieverse.core.domain.usecase.RemoteUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideLocalUseCase(localInteractor: LocalInteractor): LocalUseCase

    @Binds
    abstract fun provideRemoteUseCase(remoteInteractor: RemoteInteractor): RemoteUseCase
}