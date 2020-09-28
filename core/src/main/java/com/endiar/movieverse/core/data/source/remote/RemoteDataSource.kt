package com.endiar.movieverse.core.data.source.remote

import com.endiar.movieverse.core.data.source.remote.network.ApiResponse
import com.endiar.movieverse.core.data.source.remote.network.TMDBApiService
import com.endiar.movieverse.core.data.source.remote.response.FilmDetailResponse
import com.endiar.movieverse.core.data.source.remote.response.FilmGistResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val tmdbApiService: TMDBApiService) {

    fun getAllWeekTrending() : Flow<ApiResponse<FilmGistResponse>> {
        return flow {
            try {
                val response = tmdbApiService.getAllWeekTrending()
                if (response.results.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getNowPlaying() : Flow<ApiResponse<FilmGistResponse>> {
        return flow {
            try {
                val response = tmdbApiService.getNowPlaying(1)
                if (response.results.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailMovie(movieId: Int) : Flow<ApiResponse<FilmDetailResponse>> {
        return flow {
            try {
                val response = tmdbApiService.getMovieDetail(movieId, "videos,credits,images")
                emit(ApiResponse.Success(response))
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getTVDetail(tvId: Int) : Flow<ApiResponse<FilmDetailResponse>> {
        return flow {
            try {
                val response = tmdbApiService.getTVDetail(tvId, "videos,credits,images")
                emit(ApiResponse.Success(response))
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun searchMovie(query: String) : Flow<ApiResponse<FilmGistResponse>> {
        return flow {
            try {
                val response = tmdbApiService.searchMovie(query, 1)
                if (response.results.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun searchTV(query: String) : Flow<ApiResponse<FilmGistResponse>> {
        return flow {
            try {
                val response = tmdbApiService.searchTV(query, 1)
                if (response.results.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}