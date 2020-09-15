package com.endiar.movieverse.core.data

import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.endiar.movieverse.core.data.source.local.LocalDataSource
import com.endiar.movieverse.core.data.source.remote.DiscoverMoviePagingDataSourceFactory
import com.endiar.movieverse.core.data.source.remote.DiscoverTVPagingDataSourceFactory
import com.endiar.movieverse.core.data.source.remote.RemoteDataSource
import com.endiar.movieverse.core.data.source.remote.network.ApiResponse
import com.endiar.movieverse.core.domain.model.*
import com.endiar.movieverse.core.domain.repository.IMovieverseRepository
import com.endiar.movieverse.core.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieverseRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val discoverMoviePagingDataSourceFactory: DiscoverMoviePagingDataSourceFactory,
    private val discoverTVPagingDataSourceFactory: DiscoverTVPagingDataSourceFactory
) : IMovieverseRepository {

    override fun getTrendingFilm(): Flow<Resource<List<FilmGist>>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = remoteDataSource.getAllWeekTrending().first()) {
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(mapFilmGistResponseToListFilmGist(data)))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Success<List<FilmGist>>(listOf()))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error<List<FilmGist>>(apiResponse.errorMessage))
            }
        }
    }

    override fun getNowPlayingFilm(): Flow<Resource<List<FilmGist>>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = remoteDataSource.getNowPlaying().first()) {
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(mapFilmGistResponseToListFilmGist(data)))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Success<List<FilmGist>>(listOf()))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error<List<FilmGist>>(apiResponse.errorMessage))
            }
        }
    }

    override fun getDetailMovie(movieId: Int): Flow<Resource<FilmDetail>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = remoteDataSource.getDetailMovie(movieId).first()) {
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(mapFilmDetailResponseToFilmDetail(data)))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error<FilmDetail>(apiResponse.errorMessage))
            }
        }
    }

    override fun getDetailTV(tvId: Int): Flow<Resource<FilmDetail>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = remoteDataSource.getTVDetail(tvId).first()) {
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(mapFilmDetailResponseToFilmDetail(data)))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error<FilmDetail>(apiResponse.errorMessage))
            }
        }
    }

    override fun checkFavoriteMovie(movieId: Int): Flow<Boolean> = flow {
        val data = localDataSource.getFavoriteMovie(movieId).first()
        emit(data != null)
    }

    override suspend fun deleteFavoriteMovie(movieId: Int) {
        localDataSource.deleteFavoriteMovie(movieId)
    }

    override suspend fun insertToFavoriteMovie(movie: FilmDetail) {
        localDataSource.insertToFavoriteMovie(mapFilmDetailToFavoriteMovieEntity(movie))
    }

    override fun checkFavoriteTV(tvId: Int): Flow<Boolean> = flow {
        val data = localDataSource.getFavoriteTV(tvId).first()
        emit(data != null)
    }

    override suspend fun deleteFavoriteTV(tvId: Int) {
        localDataSource.deleteFavoriteTV(tvId)
    }

    override suspend fun insertToFavoriteTV(tv: FilmDetail) {
        localDataSource.insertToFavoriteTV(mapFilmDetailToFavoriteTVEntity(tv))
    }

    override fun getFavoriteMovieList(): Flow<List<FilmFavoriteMovie>> {
        return localDataSource.getFavoriteMovieList().map {
            mapFavoriteMovieEntityToFilmFavoriteMovie(it)
        }
    }

    override fun getFavoriteTVList(): Flow<List<FilmFavoriteTV>> {
        return localDataSource.getFavoriteTVList().map {
            mapFavoriteTVEntityToFilmFavoriteTV(it)
        }
    }

    override fun getDiscoverMovieData(): DiscoverListing<FilmGist> {
        val livePagedList = discoverMoviePagingDataSourceFactory.toLiveData(pageSize = 30)
        val refreshState = discoverMoviePagingDataSourceFactory.sourceLiveData.switchMap {
            it.initialLoad
        }

        return DiscoverListing(
            pagedList = livePagedList,
            networkState = discoverMoviePagingDataSourceFactory.sourceLiveData.switchMap {
                it.networkState
            },
            retry = {
                discoverMoviePagingDataSourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                discoverMoviePagingDataSourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

    override fun getDiscoverTVData(): DiscoverListing<FilmGist> {
        val livePagedList = discoverTVPagingDataSourceFactory.toLiveData(pageSize = 30)
        val refreshState = discoverTVPagingDataSourceFactory.sourceLiveData.switchMap {
            it.initialLoad
        }

        return DiscoverListing(
            pagedList = livePagedList,
            networkState = discoverTVPagingDataSourceFactory.sourceLiveData.switchMap {
                it.networkState
            },
            retry = {
                discoverTVPagingDataSourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                discoverTVPagingDataSourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

    override fun searchMovie(query: String): Flow<Resource<List<FilmSearch>>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = remoteDataSource.searchMovie(query).first()) {
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(mapFilmGistResponseToListFilmSearch(data)))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Success<List<FilmSearch>>(listOf()))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error<List<FilmSearch>>(apiResponse.errorMessage))
            }
        }
    }

    override fun searchTV(query: String): Flow<Resource<List<FilmSearch>>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = remoteDataSource.searchTV(query).first()) {
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(mapFilmGistResponseToListFilmSearch(data)))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Success<List<FilmSearch>>(listOf()))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error<List<FilmSearch>>(apiResponse.errorMessage))
            }
        }
    }
}