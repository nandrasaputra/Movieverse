package com.endiar.movieverse.core.domain.usecase

import com.endiar.movieverse.core.data.DiscoverListing
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.FilmDetail
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.domain.model.FilmSearch
import com.endiar.movieverse.core.domain.repository.IMovieverseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteInteractor@Inject constructor(private val repository: IMovieverseRepository): RemoteUseCase {

    override fun getTrendingFilm(): Flow<Resource<List<FilmGist>>> =
        repository.getTrendingFilm()

    override fun getNowPlayingFilm(): Flow<Resource<List<FilmGist>>> =
        repository.getNowPlayingFilm()

    override fun getDetailMovie(id: Int): Flow<Resource<FilmDetail>> =
        repository.getDetailMovie(id)

    override fun getDetailTV(id: Int): Flow<Resource<FilmDetail>> =
        repository.getDetailTV(id)

    override fun getDiscoverMovie(): DiscoverListing<FilmGist> =
        repository.getDiscoverMovieData()

    override fun getDiscoverTV(): DiscoverListing<FilmGist> =
        repository.getDiscoverTVData()

    override fun searchMovie(query: String): Flow<Resource<List<FilmSearch>>> =
        repository.searchMovie(query)

    override fun searchTV(query: String): Flow<Resource<List<FilmSearch>>> =
        repository.searchTV(query)
}