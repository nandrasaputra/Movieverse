package com.endiar.movieverse.core.domain.usecase

import com.endiar.movieverse.core.data.DiscoverListing
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.FilmDetail
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.domain.model.FilmSearch
import kotlinx.coroutines.flow.Flow

interface RemoteUseCase {
    fun getTrendingFilm(): Flow<Resource<List<FilmGist>>>
    fun getNowPlayingFilm(): Flow<Resource<List<FilmGist>>>
    fun getDetailMovie(id: Int): Flow<Resource<FilmDetail>>
    fun getDetailTV(id: Int): Flow<Resource<FilmDetail>>
    fun getDiscoverMovie(): DiscoverListing<FilmGist>
    fun getDiscoverTV(): DiscoverListing<FilmGist>
    fun searchMovie(query: String): Flow<Resource<List<FilmSearch>>>
    fun searchTV(query: String): Flow<Resource<List<FilmSearch>>>
}